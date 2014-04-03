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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Scope;
import javax.servlet.http.HttpServletResponse;
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
			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, JSONException, Exception {

		if (nextPage == 0) {
			//model.addAttribute("DuoPerson", new DuoPersonObj());
			return "DuoEnrollStep1";
		}
		
		if (nextPage == 3) {
			if (duoperson.getChoosenDevice().equals("tablet")){
				return "DuoEnrollStep4";
			}
		}

		if (nextPage == 4) {
			deviceExistDuoValidator.validate(duoperson, result);
			if (result.hasErrors()) {
				return "DuoEnrollStep3";
			}
		}

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

		return "DuoEnrollStep" + nextPage;
	}

	@RequestMapping(method = RequestMethod.POST, params = "AddUser")
	public String processSubmit(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {



		if (duoperson.getChoosenDevice() != null && !duoperson.getChoosenDevice().isEmpty() && !duoperson.getChoosenDevice().matches("landline|token")) {
			jresult = duoadminfunc.DuoEnrollUser(this.duoallikeys, "EnrollUser", duoperson.getUsername());
			model.addAttribute("barcode", jresult.getString("activation_barcode"));
		}



		userresult = duoadminfunc.duosearchuser(this.duoallikeys, "RetrUsers", duoperson.getUsername());

		for (int i = 0; i < userresult.length(); i++) {
			model.addAttribute("userinfo", userresult.getJSONObject(i));
		}



		return "DuoEnrollSuccess";

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
			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {



		//if (duoperson.getChoosenDevice()!= null && !duoperson.getChoosenDevice().isEmpty() && !duoperson.getChoosenDevice().matches("landline|token"))
		String phoneId;
		String userId;
		String qrCode;

		if (duoperson.getChoosenDevice().matches("mobile|tablet")) {

			if (duoperson.getUser_id() == null) {
				userId = duoUsrService.createObjByParam(duoperson.getUsername(), null, null);
				duoperson.setUser_id(userId);
				logger.info("Duo User Account created: " + duoperson.getUsername());
				logger.info("Duo userID: " + duoperson.getUser_id());
			}

			phoneId = duoPhoneService.createObjByParam(duoperson.getPhonenumber(), duoperson.getChoosenDevice(), duoperson.getDeviceOS());
			duoperson.setPhone_id(phoneId);
			logger.info("Duo Phone Device created: " + duoperson.getPhonenumber());
			logger.info("Duo deviceID: " + duoperson.getPhone_id());

			duoUsrService.associateObjs(duoperson.getUser_id(), duoperson.getPhone_id());
			logger.info("Successfully Linked Device to User account");

			qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");
			duoperson.setQRcode(qrCode);
			logger.info("Activation QR Code generated");


			return "DuoActivationQR";
		}


		userresult = duoadminfunc.duosearchuser(this.duoallikeys, "RetrUsers", duoperson.getUsername());

		for (int i = 0; i < userresult.length(); i++) {
			model.addAttribute("userinfo", userresult.getJSONObject(i));
		}

		return "DuoEnrollSuccess";

	}
//	@ModelAttribute("webFrameworkList")
//	public List<String> populateWebFrameworkList() {
//		
//		//Data referencing for web framework checkboxes
//		List<String> webFrameworkList = new ArrayList<String>();
//		webFrameworkList.add("Spring MVC");
//		webFrameworkList.add("Struts 1");
//		webFrameworkList.add("Struts 2");
//		webFrameworkList.add("JSF");
//		webFrameworkList.add("Apache Wicket");
//		
//		return webFrameworkList;
//	}
//	@ModelAttribute("devices")
//	public List<String> populateDeviceList() {
//		
//		//Data referencing for number radiobuttons
//		List<String> devices = new ArrayList<>();
//		devices.add("Mobile phone RECOMMENDED");
//		devices.add("Tablet (iPad, Nexus 7, etc.)");
//		devices.add("Landline");
//		devices.add("Token");
//		
//		return devices;
//	}
//	
//	@ModelAttribute("javaSkillsList")
//	public Map<String,String> populateJavaSkillList() {
//		
//		//Data referencing for java skills list box
//		Map<String,String> javaSkill = new LinkedHashMap<String,String>();
//		javaSkill.put("Hibernate", "Hibernate");
//		javaSkill.put("Spring", "Spring");
//		javaSkill.put("Apache Wicket", "Apache Wicket");
//		javaSkill.put("Struts", "Struts");
//		
//		return javaSkill;
//	}
//
//	@ModelAttribute("countryList")
//	public Map<String,String> populateCountryList() {
//		
//		//Data referencing for java skills list box
//		Map<String,String> country = new LinkedHashMap<String,String>();
//		country.put("US", "United Stated");
//		country.put("CHINA", "China");
//		country.put("SG", "Singapore");
//		country.put("MY", "Malaysia");
//		
//		return country;
//	}
}