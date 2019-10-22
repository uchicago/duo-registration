/**
 * Copyright 2015 University of Utah
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
 * Author: Brandon Gresham <brandon.gresham@utah.edu>
 */
package edu.uchicago.duo.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoObjInterface;
import edu.uchicago.duo.service.DuoUsrObjImpl;

@Controller
@RequestMapping("/secure/helpdesk")
@SessionAttributes(value = {"DuoPeople"})
public class DuoHelpdeskController {

	//get log4j handler
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private DuoObjInterface duoUsrService;

	
	@ModelAttribute("DuoPeople")
	public DuoPersonsWrapper getDuoPeople() {
		DuoPersonsWrapper duoPeople = new DuoPersonsWrapper();
		
		duoPeople.setDuoHelpdeskPerson(buildDuoHelpdeskPerson());

		// initialize with an empty enduser-person
		duoPeople.setDuoEnduserPerson(new DuoPersonObj());
		
		return duoPeople;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(HttpServletRequest request, Principal principal, ModelMap model,
			@ModelAttribute("DuoPeople") DuoPersonsWrapper duoPeople,
			HttpSession session, SessionStatus status) throws UnsupportedEncodingException, JSONException, Exception {

		if(!isAuthorizedHelpdeskPerson(duoPeople.getDuoHelpdeskPerson())) {
			logger.warn("2FA WARN - User navigated to helpdesk-area but is not authorized to act as a helpdesk user; ID:" + duoPeople.getDuoHelpdeskPerson().getChicagoID());
			status.setComplete();
			return "redirect:/secure";
		}
		
		// create a new end-user every time the form is init'd
		duoPeople.setDuoEnduserPerson(new DuoPersonObj());

		logger.info("2FA Info - " + getIPForLog(request) + " - " + "Username:" + duoPeople.getDuoHelpdeskPerson().getUsername() + "|SID:" + request.getSession().getId());

		//Initalize Model with some variables and push that into SessionAttribute	
		model.addAttribute("DuoPeople", duoPeople);

		//return form view
		return "DuoHelpdesk";
	}
	
	@RequestMapping(method = RequestMethod.POST, params="unidsearch")
	public String searchForDuoEnduserPerson(
			HttpServletRequest request, Principal principal, ModelMap model, 
			@ModelAttribute("DuoPeople") DuoPersonsWrapper duoPeople,
			BindingResult result, HttpSession session, SessionStatus status) {
		
		if(!isAuthorizedHelpdeskPerson(duoPeople.getDuoHelpdeskPerson())) {
			logger.warn("2FA WARN - User attempted to search for other user in the helpdesk-area but is not authorized to act as a helpdesk user; ID:" + duoPeople.getDuoHelpdeskPerson().getChicagoID());
			result.reject("not_authorized", "YOU ARE NOT AUTHORIZED!");
			status.setComplete();
			return "DuoHelpdesk";
		}
		
		if(StringUtils.isBlank(duoPeople.getDuoEnduserPerson().getChicagoID())) {
			result.reject("unid_not_provided", "YOU MUST PROVIDE A UNIVERSITY ID IN ORDER TO SEARCH");
			return "DuoHelpdesk";
		}
		duoPeople.getDuoEnduserPerson().setChicagoID(duoPeople.getDuoEnduserPerson().getChicagoID().trim());
		
		if(duoPeople.getDuoHelpdeskPerson().getChicagoID().equalsIgnoreCase(duoPeople.getDuoEnduserPerson().getChicagoID())) {
			result.reject("cannot_self_search", "YOU CANNOT SEARCH FOR YOURSELF");
			return "DuoHelpdesk";
		}
		
		logger.info("Helpdesk-User " + duoPeople.getDuoHelpdeskPerson().getChicagoID() + " searched for End-User " + duoPeople.getDuoEnduserPerson().getChicagoID());
		
		// go get the target-person's attributes from the Duo-service
		String duoUserId_forEndUser = duoUsrService.getObjByParam(duoPeople.getDuoEnduserPerson().getChicagoID(), null, "userId");
		if( StringUtils.isBlank(duoUserId_forEndUser)) {
			result.reject("invalid_unid", "Cannot find a user for that University ID");
			return "DuoHelpdesk";
		}
		
		duoPeople.getDuoEnduserPerson().setUser_id( duoUserId_forEndUser );
		duoPeople.getDuoEnduserPerson().setFullName( duoUsrService.getObjByParam(duoPeople.getDuoEnduserPerson().getChicagoID(), null, "fullName"));
		duoPeople.getDuoEnduserPerson().setEmail( duoUsrService.getObjByParam(duoPeople.getDuoEnduserPerson().getChicagoID(), null, "email"));
		duoPeople.getDuoEnduserPerson().setPhonenumber( duoUsrService.getObjByParam(duoPeople.getDuoEnduserPerson().getChicagoID(), null, "primaryPhoneNumber"));

		model.addAttribute("DuoPeople", duoPeople);
		
		return "DuoHelpdesk";
	}
	
	@RequestMapping(method = RequestMethod.POST, params="genbypasscode")
	public String generateBypasscodeForDuoEnduserPerson(
			HttpServletRequest request, Principal principal, ModelMap model, 
			@ModelAttribute("DuoPeople") DuoPersonsWrapper duoPeople,
			BindingResult result, HttpSession session, SessionStatus status) {

		if(!isAuthorizedHelpdeskPerson(duoPeople.getDuoHelpdeskPerson())) {
			logger.warn("2FA WARN - User attempted to generate a bypass-code in the helpdesk-area but is not authorized to act as a helpdesk user; ID:" + duoPeople.getDuoHelpdeskPerson().getChicagoID());
			result.reject("not_authorized", "YOU ARE NOT AUTHORIZED!");
			status.setComplete();
			return "DuoHelpdesk";
		}
		
		if(StringUtils.isBlank(duoPeople.getDuoEnduserPerson().getUser_id())) {
			result.reject("userid_not_provided", "BEFORE GENERATING A BYPASS-CODE, YOU MUST SEARCH FOR A USER");
			return "DuoHelpdesk";
		}
		
		if(duoPeople.getDuoHelpdeskPerson().getChicagoID().equalsIgnoreCase(duoPeople.getDuoEnduserPerson().getChicagoID())) {
			result.reject("cannot_self_search", "YOU CANNOT GENERATE A BYPASS-CODE FOR YOURSELF");
			return "DuoHelpdesk";
		}
		
		// go get the person's bypass-code from the Duo-service
		String bypassCode = ((DuoUsrObjImpl) duoUsrService).getBypassCodeForUserid( duoPeople.getDuoEnduserPerson().getUser_id());
		if(StringUtils.isBlank(bypassCode)) {
			result.reject("unable_generate_bypass_code", "Unable to generate a bypass-code for that user");
			return "DuoHelpdesk";
		}
		
		logger.info("2FA INFO - Helpdesk-User " + duoPeople.getDuoHelpdeskPerson().getChicagoID() + " has generated a bypass-code for another user " + duoPeople.getDuoEnduserPerson().getChicagoID());

		model.addAttribute("DuoPeople", duoPeople);
		model.addAttribute("DuoEndUserPerson_bypasscode", bypassCode);
		
		status.setComplete();
		
		return "DuoHelpdesk";
	}
	
	/**
	 * A simple wrapper to facilitate managing two different DuoPerson objects
	 * since Spring's 'commandName' can't deal with multiple @SessionAttributes.
 	 *
	 * Simply a way to relate the currently logged-in helpdesk-person with the
	 * enduser-person they are acting on behalf of.
	 */
	public static class DuoPersonsWrapper {
		
		private DuoPersonObj duoHelpdeskPerson;
		private DuoPersonObj duoEnduserPerson;
		
		public DuoPersonsWrapper() {
			
		}
		
		public DuoPersonObj getDuoHelpdeskPerson() {
			return this.duoHelpdeskPerson;
		}
		
		public void setDuoHelpdeskPerson(DuoPersonObj duoHelpdeskPerson) {
			this.duoHelpdeskPerson = duoHelpdeskPerson;
		}
		
		public DuoPersonObj getDuoEnduserPerson() {
			return this.duoEnduserPerson;
		}
		
		public void setDuoEnduserPerson(DuoPersonObj duoEnduserPerson) {
			this.duoEnduserPerson = duoEnduserPerson;
		}
		
		public String toString() {	
			return new ToStringBuilder(this)
					.append("HelpDesk-ChicagoID", (StringUtils.defaultIfBlank(this.getDuoHelpdeskPerson().getChicagoID(), "NULL") ) )
					.append("EndUser-ChicagoID", (StringUtils.defaultIfBlank(this.getDuoEnduserPerson().getChicagoID(), "NULL") ) )
					.append(ToStringBuilder.reflectionToString(this) )
					.toString();
		}
		
	}

	
	private DuoPersonObj buildDuoHelpdeskPerson() {
		DuoPersonObj duoHelpdeskPerson = new DuoPersonObj();
		
		//Below setting Static Attributes for Local Testing
		duoHelpdeskPerson.setChicagoID("DuoTestUserChicagoID");
		duoHelpdeskPerson.setUsername("DuoTestUser");
		duoHelpdeskPerson.setFullName("DUO Testuser");
		duoHelpdeskPerson.setEmail("testuser@duotest.com");

		//Below getting SSO Attributes for Shibboleth Support(UChicago)
		//		duoperson.setUsername(principal.getName());
		//		duoperson.setFullName(request.getHeader("givenName")+ " " + request.getHeader("sn"));
		//		duoperson.setEmail(request.getHeader("mail"));
		//		duoperson.setChicagoID(request.getHeader("chicagoID"));
				
		return duoHelpdeskPerson;
	}
	
	private boolean isAuthorizedHelpdeskPerson(DuoPersonObj duoHelpdeskPerson) {		
		// do some logic to determine if this user is authorized to act as a help-desk person		
		return true;
	}
	
	private String getIPForLog(HttpServletRequest request) {
		String sourceIPAddr = request.getRemoteAddr();
		if (sourceIPAddr == null || sourceIPAddr.startsWith("127.")) {
			sourceIPAddr = request.getHeader("x-forwarded-for");
		}

		sourceIPAddr = "[" + sourceIPAddr + "]";

		return sourceIPAddr;
	}
	
}
