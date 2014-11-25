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

import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoObjInterface;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/secure/devicemgmt")
@SessionAttributes("DuoPerson")
public class DuoDeviceMgmtController {

	protected final Log logger = LogFactory.getLog(getClass());
	//
	@Autowired
	private DuoObjInterface duoUsrService;
	//
	@Autowired
	private DuoObjInterface duoPhoneService;
	//
	@Autowired
	private DuoObjInterface duoTabletService;
	//
	@Autowired
	private DuoObjInterface duoTokenService;
	//
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

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(HttpServletRequest request, Principal principal, ModelMap model, @ModelAttribute DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

		//Below getting SSO Attributes for Shibboleth Support(UChicago)
//		duoperson.setUsername(principal.getName());
//		duoperson.setFullName(request.getHeader("givenName")+ " " + request.getHeader("sn"));
//		duoperson.setEmail(request.getHeader("mail"));
//		duoperson.setChicagoID(request.getHeader("chicagoID"));

		//Below setting Static Attributes for Local Testing
		duoperson.setUsername("DuoTestUser");
		duoperson.setFullName("DUO Testuser");
		duoperson.setEmail("testuser@duotest.com");
		logger.info("2FA Info - "+getIPForLog(request) + " - " + "Username:" + duoperson.getUsername() + "|SID:" + request.getSession().getId());

		String userId = null;

		//Check whether User is already registered in the DUO DB, if yes, record that ID, if not, forward them to first timer portal page.
		if (session.getAttribute("duoUserId") == null) {
			userId = duoUsrService.getObjByParam(duoperson.getUsername(), null, "userId");
			if (userId == null) {
				logger.info("2FA Info - "+getIPForLog(request) + " - " + duoperson.getUsername() + " landed on DeviceMgmt Page without registering with DUO!?");
				status.setComplete();
				return "redirect:/secure";
			}
			//Session Attribute "Duo User ID" - ADDED
			session.setAttribute("duoUserId", userId);
			logger.debug("2FA Debug - "+"Username:" + duoperson.getUsername() + "|DuoUserID:" + userId + "retrieved via DUO API Query");
		} else {
			userId = session.getAttribute("duoUserId").toString();
			logger.debug("2FA Debug - "+"Username:" + duoperson.getUsername() + "|DuoUserID:" + userId + "retrieved via Session Variable");
		}

		//Set Duo UserId
		duoperson.setUser_id(userId);

		//Retrieve all Devices that the user have 
		duoperson.setPhones(duoPhoneService.getAllPhones(userId));
		duoperson.setTablets(duoTabletService.getAllTablets(userId));
		duoperson.setTokens(duoTokenService.getAllTokens(userId));

		//Set DISPLAY model attributes, display related table only if device > 0
		if (duoperson.getPhones().size() > 0) {
			model.addAttribute("displayPhones", true);
			logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + " has " + duoperson.getPhones().size() + " phones");
		}

		if (duoperson.getTablets().size() > 0) {
			model.addAttribute("displayTablets", true);
			logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + " has " + duoperson.getPhones().size() + " tablets");
		}

		if (duoperson.getTokens().size() > 0) {
			model.addAttribute("displayTokens", true);
			logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + " has " + duoperson.getPhones().size() + " tokens");
		}

		//Sync Up All the attributes and push them back into Session Model Attribute
		model.addAttribute("DuoPerson", duoperson);

		//Route DUO Registered User who do not have any devices back to First timer Portal
		if (duoperson.getPhones().isEmpty() && duoperson.getTablets().isEmpty() && duoperson.getTokens().isEmpty()) {
			//return form view
			logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + " has NO registered devices in DUO");

			return "redirect:/secure";
		}

		//return form view
		return "DuoDeviceMgmt";
	}

	@RequestMapping(method = RequestMethod.POST, params = "removedevice")
	public String removeDevice(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpServletRequest request,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model) throws UnsupportedEncodingException, Exception {

		switch (duoperson.getChoosenDevice()) {
			case "Mobile":
			case "Landline":
				logger.info("2FA Info - "+getIPForLog(request) + " - " + duoperson.getUsername() + " is DELETING mobile/landline: " + duoperson.getPhonenumber());
				duoPhoneService.deleteObj(duoperson.getPhone_id(), null);
				break;
			case "Tablet":
				logger.info("2FA Info - "+getIPForLog(request) + " - " + duoperson.getUsername() + " is DELETING tablet: " + duoperson.getTabletName());
				duoTabletService.deleteObj(duoperson.getPhone_id(), null);
				break;
			case "Token":
				logger.info("2FA Info - "+getIPForLog(request) + " - " + duoperson.getUsername() + " is DELETING token: " + duoperson.getTokenSerial());
				duoTokenService.deleteObj(duoperson.getTokenId(), duoperson.getUser_id());
				break;
		}

		//return form view
		return "redirect:/secure/devicemgmt";
	}

	@RequestMapping(method = RequestMethod.POST, params = "sendsmscode")
	public String sendSMSCode(
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, BindingResult result, HttpSession session, SessionStatus status,
			ModelMap model, final RedirectAttributes redirectAttributes, HttpServletRequest request) throws UnsupportedEncodingException, Exception {

		logger.info("2FA Info - "+getIPForLog(request) + " - " + duoperson.getUsername() + " is SMSing passcode to " + duoperson.getPhonenumber());
		duoPhoneService.objActionById(duoperson.getPhone_id(), "passcodeSMS");
		
		redirectAttributes.addFlashAttribute("smsPhoneNumber", duoperson.getPhonenumber());
		redirectAttributes.addFlashAttribute("smsSent", true);
		
		//return form view
		return "redirect:/secure/devicemgmt";
	}

	/*
	 * No Real Need to do any Sanity Check for what type of OS it is for Re-activation since the DuoDeviceMgmt.jsp will only display activation option for
	 * Mobile and Tablet ONLY.
	 * Although the Button options via DuoDeviceMgmt are "ACTIVATE" and "REACTIVATE", the code is just treating them the same, which is
	 * 
	 * 1) Capture the necessary Data and store it in DuoPerson Object: Phone OS, Phone Type, Phone Number 
	 * 
	 * 2) Delete the existing Phone using the Duo Phone ID 
	 */
	@RequestMapping(method = RequestMethod.POST, params = "deviceactivation")
	public String deviceActivation( 
			@ModelAttribute("DuoPerson") DuoPersonObj duoperson, BindingResult result, HttpSession session, SessionStatus status,
			ModelMap model, final RedirectAttributes redirectAttributes, HttpServletRequest request) throws UnsupportedEncodingException, Exception {

		String temp = null;

		temp = duoperson.getChoosenDevice();
		duoperson.setChoosenDevice(temp.toLowerCase());		//DUO value is title cased, where our code is not, so just lower all cases

		temp = duoperson.getDeviceOS();
		duoperson.setDeviceOS(temp.toLowerCase());			//DUO value is title cased, where our code is not, so just lower all cases

		logger.info("2FA Info - "+getIPForLog(request) + " - " +"REACTIVATION Process-Delete First:" + duoperson.getUsername() + " is DELETING mobile/landline: " + duoperson.getPhonenumber());
		duoPhoneService.deleteObj(duoperson.getPhone_id(), null);
		duoperson.setPhone_id(null);
		logger.info("2FA Info - "+getIPForLog(request) + " - " +"REACTIVATION Process-Device Data:" + duoperson.getChoosenDevice() + '|' + duoperson.getPhonenumber() + '|' + duoperson.getDeviceOS() + " PID:" + duoperson.getPhone_id() + '|' + duoperson.getTabletName());
		
		redirectAttributes.addFlashAttribute("devReActivate", true);
		
		return "redirect:/secure/enrollment/deviceReactivation";

	}

	@RequestMapping(method = RequestMethod.POST, params = "resynctoken")
	public String resyncToken(
			@RequestParam("resyncAction") final String resyncAction, @ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			BindingResult result, HttpSession session, SessionStatus status, ModelMap model, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) throws UnsupportedEncodingException, Exception {

		switch (resyncAction) {
			case "input":
				return "DuoResyncToken";
			case "resync":
				logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + "|Token: " + duoperson.getTokenSerial() + " |Resync(Code1):" + duoperson.getTokenSyncCode1());
				logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + "|Token: " + duoperson.getTokenSerial() + " |Resync(Code2):" + duoperson.getTokenSyncCode2());
				logger.debug("2FA Debug - "+getIPForLog(request) + " - " + duoperson.getUsername() + "|Token: " + duoperson.getTokenSerial() + " |Resync(Code3):" + duoperson.getTokenSyncCode3());

				/*
				 * Calling validator manually, if using the @validated automatically, see below example:
				 * @Validated({DuoPersonObj.TokenResyncValidation.class})
				 */
				validator.validate(duoperson, result, DuoPersonObj.TokenResyncValidation.class);

				if (result.hasErrors()) {
					return "DuoResyncToken";
				}
				break;
		}
		duoTokenService.resyncObj(duoperson.getTokenId(), duoperson.getTokenSyncCode1(), duoperson.getTokenSyncCode2(), duoperson.getTokenSyncCode3());
		logger.info("2FA Info - "+getIPForLog(request) + " - " + duoperson.getUsername() + "|Token: " + duoperson.getTokenSerial() + " RESYNC SUCCESS");
		redirectAttributes.addFlashAttribute("resyncTokenId", duoperson.getTokenId());
		redirectAttributes.addFlashAttribute("resyncTokenSN", duoperson.getTokenSerial());
		redirectAttributes.addFlashAttribute("resyncsuccess", true);

		//return form view
		return "redirect:/secure/devicemgmt";
	}

	@RequestMapping(method = RequestMethod.POST, params = "cancel")
	public String cancel(ModelMap model, @ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

		status.setComplete();
		return "redirect:/secure/devicemgmt";
	}

	//May not need it anylonger
	@RequestMapping(method = RequestMethod.POST, params = "home")
	public String goHome(ModelMap model, @ModelAttribute("DuoPerson") DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

		status.setComplete();
		return "redirect:/secure";
	}
}
