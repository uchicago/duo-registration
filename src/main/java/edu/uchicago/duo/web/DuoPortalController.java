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
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/secure")
@SessionAttributes("DuoPerson")
public class DuoPortalController {

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

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(HttpServletRequest request, Principal principal, ModelMap model, @ModelAttribute DuoPersonObj duoperson, HttpSession session, SessionStatus status) {

		String userId = null;

		String sourceIPAddr = request.getRemoteAddr();
		if (sourceIPAddr == null || sourceIPAddr.startsWith("127.")) {
			sourceIPAddr = request.getHeader("x-forwarded-for");
		}

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

		if (session.getAttribute("duoUserId") == null) {
			userId = duoUsrService.getObjByParam(duoperson.getUsername(), null, "userId");
			if (userId == null) {
				logger.info("2FA Info - "+getIPForLog(request) + " - " + "Username:" + duoperson.getUsername() + " has not yet register with DUO!");
				model.addAttribute("DuoPerson", duoperson);		//Initalize Model with some variables and push that into SessionAttribute
				return "DuoPortal";		//return form view
			}
			logger.debug("2FA Debug - "+"Assigned UserID via DUO API Query");
		} else {
			userId = session.getAttribute("duoUserId").toString();
			logger.debug("2FA Debug - "+"Assigned UserID via Session Variable");
		}

		duoperson.setUser_id(userId);
		duoperson.setPhones(duoPhoneService.getAllPhones(userId));
		duoperson.setTablets(duoTabletService.getAllTablets(userId));
		duoperson.setTokens(duoTokenService.getAllTokens(userId));

		//Initalize Model with some variables and push that into SessionAttribute
		model.addAttribute("DuoPerson", duoperson);

		if (duoperson.getPhones().isEmpty() && duoperson.getTablets().isEmpty() && duoperson.getTokens().isEmpty()) {
			//return form view
			return "DuoPortal";
		} else {
			//return form view
			return "redirect:/secure/devicemgmt";
		}

	}

	@RequestMapping(method = RequestMethod.POST, params = "wheretogo")
	public String whereToGo(@RequestParam("_destination") final String dest, @ModelAttribute("DuoPerson") DuoPersonObj duoperson,
			HttpServletRequest request, Principal principal, BindingResult result, HttpSession session, SessionStatus status, ModelMap model) {

		String goTo = null;

		switch (dest) {
			case "register":
				goTo = "redirect:/secure/enrollment";
				break;
			case "devicemgmt":
				goTo = "redirect:/secure/devicemgmt";
				break;
//			case "2FAoptin":
//				duoperson.setOptInStatus(true);
//				goTo = "redirect:/secure/enrollment/2FAoptin";
//				break;
		}

		return goTo;
	}
}