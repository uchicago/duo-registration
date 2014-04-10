/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */
package edu.uchicago.duo.web;

import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoAdminFunc;
import edu.uchicago.duo.service.DuoObjInterface;
import edu.uchicago.duo.validator.DeviceExistDuoValidator;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/enrollment")
@SessionAttributes("DuoPerson")
public class DuoEnrollController {

	//get log4j handler
	private static final Logger logger = Logger.getLogger(DuoEnrollController.class);
	///
	@Autowired
	private DuoAdminFunc duoadminfunc;
	///
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoallikeys;
	///
	@Autowired
	private DuoObjInterface duoPhoneService;
	///
	@Autowired
	private DuoObjInterface duoUsrService;
	///
	@Autowired
	private DeviceExistDuoValidator deviceExistDuoValidator;
	///
	private JSONObject jresult = null;
	private JSONArray userresult = null;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

	}

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model, @ModelAttribute DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

//		DuoPersonObj duopersonobj = new DuoPersonObj();
//		duopersonobj.setPhonenumber("7731234567");
//		model.addAttribute("duopersonobj", duopersonobj);

//		duoperson.setUsername(session.getAttribute("username").toString());
		duoperson.setChoosenDevice("mobile");
		duoperson.setDeviceOS("apple ios");
		model.addAttribute("DuoPerson", duoperson);

		//return form view
		return "DuoEnrollStep1";
	}

	@RequestMapping(method = RequestMethod.POST, params = "reset")
	public String resetform(ModelMap model, @ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

		status.setComplete();
		return "duo";
	}

	@RequestMapping(method = RequestMethod.POST, params = "enrollsteps")
	public String processPage(@RequestParam("_page") final int nextPage,
			@Valid @ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, JSONException, Exception {

		//Redirect for Enroll Another Device
		if (nextPage == 0) {
			//model.addAttribute("DuoPerson", new DuoPersonObj());		//Thinking about emptying all info of Bean or just some?
			return "DuoEnrollStep1";
		}

		if (nextPage == 2) {
			//Session Attribute "Username" - UPDATED
			session.setAttribute("username", duoperson.getUsername());

			String userId = duoUsrService.getObjByParam(duoperson.getUsername(), null, "userId");
			duoperson.setUser_id(userId);

			if (userId != null) {
				//Session Attribute "Duo User ID" - UPDATED (Depends)
				session.setAttribute("duoUserId", userId);
				model.put("existingUser", true);
			}
		}

		//Redirect Depends on the type of Device that is being enroll
		if (nextPage == 3) {
			switch (duoperson.getChoosenDevice()) {
				case "tablet":
					duoperson.setDeviceOS(null);
					return "DuoEnrollTablet";
//					return "DuoEnrollStep4";
				case "token":
					return "DuoEnrollToken";
			}

		}

		//Validation on Submission of Phone Nmber, to make sure Phone Number has not been registered or belong to someone else
		if (nextPage == 4) {
			deviceExistDuoValidator.validate(duoperson, result);
			if (result.hasErrors()) {
				return "DuoEnrollStep3";
			}
		}

		//Check on Activation Status on DUO Mobile App, don't let user move on until confirm the device has been activated
		if (nextPage == 5) {
			String activeStatus;
			activeStatus = duoPhoneService.getObjStatusById(duoperson.getPhone_id());

			if (activeStatus.equals("false")) {
				if (duoperson.getQRcode() == null) {
					String qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");
					duoperson.setQRcode(qrCode);
				}
				logger.info("DeviceID:" + duoperson.getPhone_id() + "|| Not Activated");
				model.put("deviceNotActive", true);
				return "DuoActivationQR";
			}

			userresult = duoadminfunc.duosearchuser(this.duoallikeys, "RetrUsers", duoperson.getUsername());
			model.addAttribute("userinfo", userresult.getJSONObject(0));
			return "DuoEnrollSuccess";
		}

		//Traverse Multipage Form, DuoEnrollStep*.jsp
		return "DuoEnrollStep" + nextPage;
	}

	@RequestMapping(method = RequestMethod.POST, params = "sendsms")
	public String duoSendSMS(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {

		duoPhoneService.objActionById(duoperson.getPhone_id(), "activationSMS");

		duoperson.setQRcode(null);

		logger.info("Landed on DuoSend SMS link!");
		return "DuoActivationQR";

	}

	@RequestMapping(method = RequestMethod.POST, params = "enrollUserNPhone")
	public String processEnroll(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {

		String phoneId;
		String userId;
		String qrCode;

		//Register First-Time User into DUO Database
		if (duoperson.getUser_id() == null) {
			userId = duoUsrService.createObjByParam(duoperson.getUsername(), duoperson.getFullName(), duoperson.getEmail(), null);
			duoperson.setUser_id(userId);

			//Session Attribute "Duo User ID" - ADDED
			session.setAttribute("duoUserId", userId);

			logger.info("Duo User Account created: " + duoperson.getUsername());
			logger.info("Duo userID: " + duoperson.getUser_id());
		}

		/**
		 * Enrollment Procedure for Type == Mobile | Tablet 1st) Create the
		 * Phone/Tablet Device first in DUO DB 2nd) Link the newly create
		 * Phone/Tablet to the user 3rd) Generate and Display the Activation QR
		 * code for DUO Mobile App Registration
		 */
		if (duoperson.getChoosenDevice().matches("mobile|tablet")) {
			logger.info(duoperson.getChoosenDevice() + ' ' + duoperson.getDeviceOS() + ' ' + duoperson.getTabletName());
			phoneId = duoPhoneService.createObjByParam(duoperson.getPhonenumber(), duoperson.getChoosenDevice(), duoperson.getDeviceOS(), duoperson.getTabletName());
			duoperson.setPhone_id(phoneId);
			logger.info("Duo Phone Device created: " + duoperson.getPhonenumber());
			logger.info("Duo deviceID: " + duoperson.getPhone_id());

			duoPhoneService.associateObjs(duoperson.getUser_id(), duoperson.getPhone_id());

			qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");
			duoperson.setQRcode(qrCode);

			return "DuoActivationQR";
		}

		/**
		 * Enrollment Procedure for Type == Token 1st) Validate against the
		 * database to see whether Token has been register by somebody else ||
		 * Token Existence in DB
		 */
		if (duoperson.getChoosenDevice().matches("token")) {
			logger.info("Duo Token Type:" + duoperson.getTokenType());
			logger.info("Duo Token Serial Number:" + duoperson.getTokenSerial());

			deviceExistDuoValidator.validate(duoperson, result);
			if (result.hasErrors()) {
				return "DuoEnrollToken";
			}
			////////////
			////////Need to Get the Token ID First!!!!!!
			/////// Then duoTokenService.associateObjs
			////////////

		}


		userresult = duoadminfunc.duosearchuser(this.duoallikeys, "RetrUsers", duoperson.getUsername());
		for (int i = 0; i < userresult.length(); i++) {
			model.addAttribute("userinfo", userresult.getJSONObject(i));
		}
		return "DuoEnrollSuccess";

	}

	@ModelAttribute("tokenTypeList")
	public Map<String, String> populateTokenTypeList() {

		Map<String, String> tokenType = new LinkedHashMap<>();
		tokenType.put("d1", "Duo-D100 hardware token");
		tokenType.put("yk", "YubiKey AES hardware token");
		tokenType.put("h6", "HOTP-6 hardware token");
		tokenType.put("h8", "HOTP-8 hardware token");
		tokenType.put("t6", "TOTP-6 hardware token");
		tokenType.put("t8", "TOTP-8 hardware token");

		return tokenType;
	}

	@ModelAttribute("tabletOSList")
	public Map<String, String> populateTabletOSList() {

		Map<String, String> tabletOS = new LinkedHashMap<>();
		tabletOS.put("apple ios", "Apple IOS");
		tabletOS.put("google android", "Google Android");
		tabletOS.put("windows phone", "Microsoft Windows for Surface");

		return tabletOS;
	}
//	@RequestMapping(method = RequestMethod.POST, params = "AddUser")
//	public String processSubmit(
//			@ModelAttribute("DuoPerson") DuoPersonObj duoperson,
//			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {
//
//		if (duoperson.getChoosenDevice() != null && !duoperson.getChoosenDevice().isEmpty() && !duoperson.getChoosenDevice().matches("landline|token")) {
//			jresult = duoadminfunc.DuoEnrollUser(this.duoallikeys, "EnrollUser", duoperson.getUsername());
//			model.addAttribute("barcode", jresult.getString("activation_barcode"));
//		}
//
//		userresult = duoadminfunc.duosearchuser(this.duoallikeys, "RetrUsers", duoperson.getUsername());
//
//		for (int i = 0; i < userresult.length(); i++) {
//			model.addAttribute("userinfo", userresult.getJSONObject(i));
//		}
//		return "DuoEnrollSuccess";
//	}
}