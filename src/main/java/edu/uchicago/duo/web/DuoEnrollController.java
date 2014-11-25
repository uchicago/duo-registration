/**
 * Copyright 2014 University of Chicago
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Author: Daniel Yu <danielyu@uchicago.edu>
 */
package edu.uchicago.duo.web;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoObjInterface;
import edu.uchicago.duo.validator.DeviceExistDuoValidator;
import static edu.uchicago.duo.web.DuoEnrollController.sortByValue;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Date;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.Locale;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/secure/enrollment")
@SessionAttributes("DuoPerson")
public class DuoEnrollController {

	//get log4j handler
	protected final Log logger = LogFactory.getLog(getClass());
	///
	@Autowired
	private DuoObjInterface duoPhoneService;
	///
	@Autowired
	private DuoObjInterface duoUsrService;
	///
	@Autowired
	private DuoObjInterface duoTokenService;
	///
	@Autowired
	private DeviceExistDuoValidator deviceExistDuoValidator;
	///
	@Autowired
	SmartValidator validator;

	/**
	 * **********************************************************
	 *
	 * Private Methods Below
	 *
	 ***********************************************************
	 */
	private String getIPForLog(HttpServletRequest request) {
		String sourceIPAddr = request.getRemoteAddr();
		if (sourceIPAddr == null || sourceIPAddr.startsWith("127.")) {
			sourceIPAddr = request.getHeader("x-forwarded-for");
		}

		sourceIPAddr = "[" + sourceIPAddr + "]";

		return sourceIPAddr;
	}

	/**
	 * **********************************************************
	 *
	 * Spring Controller Methods Below
	 *
	 ***********************************************************
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/*
	 * Below initalize the model and return the view, Setup NEW session Attribute for "DuoPerson",
	 * according to Spring Docs, it is recommended to NOT share session attributes among different controller.
	 * It makes sense to me in that when user bookmarked this specific entry point, sharing a common session attribute may not 
	 * get all the value the controller needed.
	 * Note about session attribute:
	 * @ModelAttribute("DuoPerson") DuoPersonObj duoperson  <Look for existing session attribute>
	 * @ModelAttribute DuoPersonObj duoperson <Initalize a New session attribute>
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(HttpServletRequest request, Principal principal, ModelMap model, @ModelAttribute DuoPersonObj duoperson,
			HttpSession session, SessionStatus status) throws UnsupportedEncodingException, JSONException, Exception {

		//Below getting SSO Attributes for Shibboleth Support(UChicago)
//		duoperson.setUsername(principal.getName());
//		duoperson.setFullName(request.getHeader("givenName")+ " " + request.getHeader("sn"));
//		duoperson.setEmail(request.getHeader("mail"));
//		duoperson.setChicagoID(request.getHeader("chicagoID"));

		//Below setting Static Attributes for Local Testing
		duoperson.setUsername("DuoTestUser");
		duoperson.setFullName("DUO Testuser");
		duoperson.setEmail("testuser@duotest.com");


		logger.info("2FA Info - " + getIPForLog(request) + " - " + "Username:" + duoperson.getUsername() + "|SID:" + request.getSession().getId());

		//Setup Default Selection Values for the wizard form
		duoperson.setChoosenDevice("mobile");
		duoperson.setDeviceOS("apple ios");
		duoperson.setCountryDialCode("+1");

		//Initalize Model with some variables and push that into SessionAttribute
		model.addAttribute("DuoPerson", duoperson);

		//return form view
		return processPage(2, duoperson, request, principal, null, session, status, model);
	}

	@RequestMapping(method = RequestMethod.POST, params = "reset")
	public String resetform(ModelMap model, @ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

		status.setComplete();
		return "redirect:/secure";
	}

	@RequestMapping(method = RequestMethod.POST, params = "back")
	public String goBack(@RequestParam("_backpage") final int backPage, ModelMap model, @ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, SessionStatus status) {
		if (backPage == 31) {
			return "DuoEnrollTablet";
		}

		return "DuoEnrollStep" + backPage;
	}

	@RequestMapping(method = RequestMethod.POST, params = "enrollsteps")
	public String processPage(@RequestParam("_page") final int nextPage,
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpServletRequest request, Principal principal,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, JSONException, Exception {

		//Redirect for Enroll Another Device
		if (nextPage == 0) {
			status.setComplete();
			return "redirect:/secure/enrollment";
		}

		if (nextPage == 2) {
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
				case "token":
					return "DuoEnrollToken";
			}

		}

		if (nextPage == 31) {
			validator.validate(duoperson, result, DuoPersonObj.TabletInfoValidation.class);
			if (result.hasErrors()) {
				return "DuoEnrollTablet";
			}
			return "DuoEnrollStep5";
		}

		if (nextPage == 32) {
			validator.validate(duoperson, result, DuoPersonObj.TokenInfoValidation.class);
			if (result.hasErrors()) {
				return "DuoEnrollToken";
			}
			return processEnroll(request, duoperson, result, session, status, model);
		}


		//Validation on Submission of Phone Number, to make sure Phone Number has not been registered or belong to someone else
		if (nextPage == 4) {

			validator.validate(duoperson, result, DuoPersonObj.PhoneNumberValidation.class);
			if (result.hasErrors()) {
				return "DuoEnrollStep3";
			}

			deviceExistDuoValidator.validate(duoperson, result);
			if (result.hasErrors()) {
				return "DuoEnrollStep3";
			}

			if (duoperson.getChoosenDevice().equals("landline")) {
				return "DuoPhoneVerify";
			}
		}

		if (nextPage == 5) {
			if (duoperson.getDeviceOS().equals("unknown")) {
				return "DuoPhoneVerify";
			}
		}


		//Check on Activation Status on DUO Mobile App, don't let user move on until confirm the device has been activated
		if (nextPage == 6) {
			String activeStatus;
			activeStatus = duoPhoneService.getObjStatusById(duoperson.getPhone_id());

			if (activeStatus.equals("false")) {
				if (duoperson.getQRcode() == null) {
					String qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");
					duoperson.setQRcode(qrCode);
				}
				logger.error("2FA Error - " + getIPForLog(request) + " - " + duoperson.getUsername() + "|DeviceID: " + duoperson.getPhone_id() + " NOT ACTIVATED");
				model.put("deviceNotActive", true);
				return "DuoActivationQR";
			}
			model.put("deviceActive", "Yes");
			logger.info("2FA Info - " + getIPForLog(request) + " - " + duoperson.getUsername() + " Successfully Registered: PhoneNumber:" + duoperson.getPhonenumber()
					+ " Tablet Name:" + duoperson.getTabletName());
			return "DuoEnrollSuccess";
		}

		//Traverse Multipage Form, DuoEnrollStep*.jsp
		return "DuoEnrollStep" + nextPage;
	}

	@RequestMapping(value = "/phoneverify.json/{action}", method = RequestMethod.GET)
	@ResponseBody
	public String callToVerify(@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, @PathVariable String action) {
		String callInfo = null;
		String callState = null;
		Map<String, Object> verifyInfo = new HashMap<>();

		switch (action) {
			case "call":
				verifyInfo = duoPhoneService.verifyObj(duoperson.getCompletePhonenumber(), duoperson.getLandLineExtension(), action);
				duoperson.setPhoneVerifyPin(verifyInfo.get("pin").toString());
				duoperson.setPhoneVerifyTxid(verifyInfo.get("txid").toString());
				return "CALLING";
			case "status":
				verifyInfo = duoPhoneService.verifyObj(duoperson.getPhoneVerifyTxid(), null, action);
				callInfo = verifyInfo.get("info").toString();
				callState = verifyInfo.get("state").toString();
				break;
		}

		return callInfo;
	}

	@RequestMapping(value = "/phoneverify.json/verify/{inputpin}", method = RequestMethod.GET)
	@ResponseBody
	public String verifyPin(@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, @PathVariable String inputpin, HttpServletRequest request) {
		String correctPin = null;

		correctPin = duoperson.getPhoneVerifyPin();

		if (inputpin.equals(correctPin)) {
			duoperson.setPhoneOwnerVerified(true);
			logger.info("2FA Info - " + getIPForLog(request) + " - " + "Username:" + duoperson.getUsername() + " VERIFIED PhoneNumber:" + duoperson.getPhonenumber() + " Extension:" + duoperson.getLandLineExtension());
			return "VERIFIED";
		} else {
			duoperson.setPhoneOwnerVerified(false);
			return "INCORRECT";
		}
	}

	@RequestMapping("/activationstatus.json")
	@ResponseBody
	public String reportStatus(@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session) {

		return duoPhoneService.getObjStatusById(duoperson.getPhone_id());

	}

	@RequestMapping(method = RequestMethod.POST, params = "sendsms")
	public String duoSendSMS(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpServletRequest request,
			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {

		duoPhoneService.objActionById(duoperson.getPhone_id(), "activationSMS");

		logger.info("2FA Info - " + getIPForLog(request) + " - " + duoperson.getUsername() + " is sending ACTIVATION SMS to " + duoperson.getPhonenumber());

		duoperson.setQRcode(null);

		return "DuoActivationQR";

	}

	@RequestMapping(method = RequestMethod.POST, params = "genQRcode")
	public String genQRCode(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpServletRequest request,
			BindingResult result, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {

		String qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");

		duoperson.setQRcode(qrCode);

		logger.debug("2FA Debug - " + getIPForLog(request) + " - " + duoperson.getUsername() + "|DeviceID: " + duoperson.getPhone_id() + " QR code regenrated");

		return "DuoActivationQR";

	}

	@RequestMapping(value = "/2FAoptin")
	public String twoFactorOptIn(@RequestParam(value = "_action", required = false, defaultValue = "landed") final String action,
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, HttpServletRequest request) {

		String result = null;

		switch (action) {
			case "landed":
				duoperson.setOptInStatus(true);
				break;
			case "optin":
				if (duoperson.isOptInStatus()) {
					result = duoUsrService.objActionById(duoperson.getChicagoID(), "AddUserToDuoForce");
					if (result.equals("Y")) {
						duoperson.setOptInStatus(true);
						logger.info("2FA Info - " + getIPForLog(request) + " - " + duoperson.getUsername() + " has OPT-IN DuoForce Grouper Group");
						return "DuoEnroll2FAOptResult";
					} else {
						duoperson.setOptInStatus(false);
						return "DuoEnroll2FAOptResult";		//May be Routing to an Error page instead!!!
					}
				} else {
					return "redirect:/secure";
				}
		}

		return "DuoEnroll2FAOptIn";
	}

	@RequestMapping(value = "/2FAoptout")
	public String twoFactorOptOut(@RequestParam(value = "_action", required = false, defaultValue = "landed") final String action,
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, HttpServletRequest request) {

		String result = null;

		switch (action) {
			case "landed":
				duoperson.setOptInStatus(true);
				break;
			case "optout":
				if (duoperson.isOptInStatus()) {
					return "redirect:/secure";
				} else {
					result = duoUsrService.objActionById(duoperson.getChicagoID(), "RemoveUserFromDuoForce");
					if (result.equals("Y")) {
						duoperson.setOptInStatus(false);
						logger.info("2FA Info - " + getIPForLog(request) + " - " + duoperson.getUsername() + " has OPT-OUT DuoForce Grouper Group");
						return "DuoEnroll2FAOptResult";
					} else {
						duoperson.setOptInStatus(true);
						return "DuoEnroll2FAOptResult";		//May be Routing to an Error page instead!!!
					}
				}
		}

		return "DuoEnroll2FAOptOut";
	}

	/**
	 * *********************************************************************
	 * Calling by DuoDeviceMgmt Controller, for Device Reactivation Purposes
	 * **********************************************************************
	 */
	@RequestMapping(value = "/deviceReactivation")
	public String deviceReactivation(@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpServletRequest request, Principal principal,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, JSONException, Exception {

		logger.info("2FA Info - " + getIPForLog(request) + " - " + "REACTIVATION Process-Device Data:" + duoperson.getChoosenDevice() + ' ' + duoperson.getPhonenumber() + ' ' + duoperson.getDeviceOS());
		return "DuoEnrollStep5";

	}

	/**
	 * ************************************************************************
	 * Main Method to Handle the actual Device ID Creation and Association
	 * between the Device and User
	 * *************************************************************************
	 */
	@RequestMapping(method = RequestMethod.POST, params = "enrollUserNPhone")
	public String processEnroll(
			HttpServletRequest request, @ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			//			@RequestParam(value = "_unknownMobileVerified", required = false, defaultValue = "N") final String unknownMobileVerified,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {

		String phoneId;
		String tokenId;
		String userId;
		String qrCode;

		/**
		 * *******************************************************************************************
		 * Enrollment Procedure, first thing first!
		 *
		 * Check Whether User is a registered DUO User and Register First-Time
		 * User into DUO Database
		 * ********************************************************************************************
		 */
		if (duoperson.getUser_id() == null) {
			userId = duoUsrService.createObjByParam(duoperson.getUsername(), duoperson.getFullName(), duoperson.getEmail(), null, null);
			duoperson.setUser_id(userId);

			//Session Attribute "Duo User ID" - ADDED
			session.setAttribute("duoUserId", userId);

			logger.info("2FA Info - " + getIPForLog(request) + " - " + "Duo User Account created for: " + duoperson.getUsername() + "|DuoUserID:" + duoperson.getUser_id());
		}

		//Attempts to try to Catpure F5/Browser Refresh during Device Activation, Not letting Users jump out without Activation success
		if (StringUtils.hasLength(duoperson.getPhone_id())) {
			String activeStatus = duoPhoneService.getObjStatusById(duoperson.getPhone_id());
			if (activeStatus.equals("false")) {
				qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");
				duoperson.setQRcode(qrCode);
				model.put("deviceNotActive", true);
				return "DuoActivationQR";
			} else {
				logger.info("2FA Info - " + getIPForLog(request) + " - " + duoperson.getUsername() + " Successfully Registered: PhoneNumber:" + duoperson.getPhonenumber() + "Tablet Name:" + duoperson.getTabletName());
				return "DuoEnrollSuccess";
			}
		}

		/**
		 * ***********************************************************************************
		 * Enrollment Procedure for Type == unknown
		 *
		 * Unknown == Old CellPhone that doesn't support Duo Mobile App, but
		 * still support SMS
		 * ***********************************************************************************
		 */
		if (duoperson.getChoosenDevice().matches("mobile") && duoperson.getDeviceOS().matches("unknown")) {

			phoneId = duoPhoneService.createObjByParam(duoperson.getCompletePhonenumber(), duoperson.getChoosenDevice(), duoperson.getDeviceOS(), null, null);
			duoperson.setPhone_id(phoneId);
			duoPhoneService.associateObjs(duoperson.getUser_id(), phoneId);
		}

		/**
		 * ********************************************************************
		 * Enrollment Procedure for Type == Mobile | Tablet
		 *
		 * 1st) Create the Phone/Tablet Device first in DUO DB
		 *
		 * 2nd) Link the newly create Phone/Tablet to the user
		 *
		 * 3rd) Generate and Display the Activation QR code for DUO Mobile App
		 * Registration
		 * ********************************************************************
		 */
		if (duoperson.getChoosenDevice().matches("mobile|tablet") && !duoperson.getDeviceOS().matches("unknown")) {

			phoneId = duoPhoneService.createObjByParam(duoperson.getCompletePhonenumber(), duoperson.getChoosenDevice(), duoperson.getDeviceOS(), duoperson.getTabletName(), null);
			duoperson.setPhone_id(phoneId);

			duoPhoneService.associateObjs(duoperson.getUser_id(), phoneId);

			qrCode = duoPhoneService.objActionById(duoperson.getPhone_id(), "qrCode");
			duoperson.setQRcode(qrCode);

			return "DuoActivationQR";
		}


		/**
		 * ******************************************
		 * Enrollment Procedure for Type == LandLine
		 * ******************************************
		 */
		if (duoperson.getChoosenDevice().matches("landline")) {
			phoneId = duoPhoneService.createObjByParam(duoperson.getCompletePhonenumber(), duoperson.getChoosenDevice(), null, null, duoperson.getLandLineExtension());
			duoperson.setPhone_id(phoneId);
			duoPhoneService.associateObjs(duoperson.getUser_id(), phoneId);
		}


		/**
		 * ****************************************************************
		 * Enrollment Procedure for Type == Token
		 *
		 * 1) Validate against the database to see whether Token has been
		 * register by somebody else || Token Existence in DB
		 *
		 * 2) Then ASSOCIATE the Token with the User
		 * ****************************************************************
		 */
		if (duoperson.getChoosenDevice().matches("token")) {

			logger.debug("2FA Debug - " + getIPForLog(request) + "|" + duoperson.getUsername() + " is registering Token:" + duoperson.getTokenType() + "/" + duoperson.getTokenSerial());

			deviceExistDuoValidator.validate(duoperson, result);
			if (result.hasErrors()) {
				return "DuoEnrollToken";
			}
			tokenId = duoTokenService.getObjByParam(duoperson.getTokenSerial(), duoperson.getTokenType(), "tokenId");
			duoperson.setTokenId(tokenId);
			duoTokenService.associateObjs(duoperson.getUser_id(), tokenId);

			String readabletype = null;
			switch (duoperson.getTokenType()) {
				case "d1":
					readabletype = "Duo-D100";
					break;
				case "yk":
					readabletype = "YubiKey AES";
					break;
				case "h6":
					readabletype = "HOTP-6";
					break;
				case "h8":
					readabletype = "HOTP-8";
					break;
				case "t6":
					readabletype = "TOTP-6";
					break;
				case "t8":
					readabletype = "TOTP-8";
					break;
			}
			duoperson.setTokenType(readabletype);
		}

		model.put("deviceActive", "Yes");
		logger.info("2FA Info - " + getIPForLog(request) + " - " + duoperson.getUsername() + " Successfully Registered: PhoneNumber:" + duoperson.getPhonenumber()
				+ " Tablet Name:" + duoperson.getTabletName() + " Token SN:" + duoperson.getTokenSerial());
		return "DuoEnrollSuccess";
	}

	/**
	 * ****************************************
	 * Drop Down List for Token Type Selection
	 *
	 * Used in: DuoEnrollToken.jsp
	 *
	 *****************************************
	 */
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

	/**
	 * ****************************************
	 * Drop Down List for Tablet OS Selection
	 *
	 * Used in: DuoEnrollTablet.jsp
	 *
	 ******************************************
	 */
	@ModelAttribute("tabletOSList")
	public Map<String, String> populateTabletOSList() {

		Map<String, String> tabletOS = new LinkedHashMap<>();
		tabletOS.put("apple ios", "Apple IOS");
		tabletOS.put("google android", "Google Android");
//		tabletOS.put("windows phone", "Microsoft Windows for Surface");

		return tabletOS;
	}

	/**
	 * *********************************************************************
	 * Below are ALL FOR creating the International Dial Code Drop Down list
	 *
	 * Used in: DuoEnrollStep3.jsp
	 * *********************************************************************
	 */
	@ModelAttribute("countryDialList")
	public Map<String, String> populatecountryDialList() {

		List<DuoEnrollController.Country> countries = new ArrayList<>();
		Map<String, String> dialCodes = new LinkedHashMap<>();
		Map<String, String> sortedDialCodes = new LinkedHashMap<>();

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

		//
		// Get ISO countries, create Country object and
		// store in the collection.
		//
		String[] isoCountries = Locale.getISOCountries();
		for (String country : isoCountries) {
			Locale locale = new Locale("en", country);
			String code = locale.getCountry();
			String name = locale.getDisplayCountry();

			if (!"".equals(code) && !"".equals(name)) {
				try {
					int dialCode = phoneUtil.parse("1112223333", code).getCountryCode();
					countries.add(new DuoEnrollController.Country(code, name, dialCode));
				} catch (Exception e) {
				}
			}
		}

		Collections.sort(countries, new DuoEnrollController.CountryComparator());

		for (DuoEnrollController.Country country : countries) {
			dialCodes.put("+" + String.valueOf(country.dialCode), country.name);
			//dialCodes.put("+"+String.valueOf(country.code), country.name);
		}

		sortedDialCodes = sortByValue(dialCodes);

		return sortedDialCodes;
	}

	private static class Country {

		private String code;
		private String name;
		private int dialCode;

		Country(String code, String name, int dialCode) {
			this.code = code;
			this.name = name;
			this.dialCode = dialCode;
		}
	}

	static class CountryComparator implements Comparator {

		private Comparator comparator;

		CountryComparator() {
			comparator = Collator.getInstance();
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {
			DuoEnrollController.Country c1 = (DuoEnrollController.Country) o1;
			DuoEnrollController.Country c2 = (DuoEnrollController.Country) o2;

			return comparator.compare(c1.name, c2.name);
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list =
				new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}