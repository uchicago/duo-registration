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

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("index.htm")
public class DuoController {

	protected final Log logger = LogFactory.getLog(getClass());
	private JSONObject result = null;
	private Date date = new Date();
	String name = null;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)
			throws ServletException, IOException, JSONException, Exception {

		Map<String, Object> myModel = new HashMap<>();
//		date.setTime((long) result.getInt("mintime") * 1000);
		myModel.put("mintime", date);
		return new ModelAndView("2FALandingPage", "duotest", myModel);
//		return new ModelAndView("DuoEnrollStep5", "duotest", myModel);

//		logger.info("Returning Duo View");
//		name = principal.getName();
//		myModel.put("username", name);
//		session.setAttribute("username", name);
//		HttpSession.setAttribute();
	}

	@RequestMapping(method = RequestMethod.POST, params = "wheretogo")
	public String whereToGo(@RequestParam("_destination") final String dest) {

		String goTo = null;

		switch (dest) {
			case "portal":
				goTo = "redirect:/secure";
				break;
		}

		return goTo;
	}
}