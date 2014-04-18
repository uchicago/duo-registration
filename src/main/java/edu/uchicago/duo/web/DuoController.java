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

import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoAdminFunc;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("index.htm")

public class DuoController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private DuoAdminFunc duoadminfunc;
	
	
	@Autowired(required =true)
	private DuoAllIntegrationKeys duoallikeys;
	
	private JSONObject result = null;
	private JSONArray userresult = null;
	private Date date = new Date();
	String name = null;
	

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)
			throws ServletException, IOException, JSONException, Exception {

		logger.info("Returning Duo View");

		Map<String, Object> myModel = new HashMap<String, Object>();
		
		name = principal.getName();
//		myModel.put("username", name);
		
		session.setAttribute("username", name);
//		HttpSession.setAttribute();

		//Make "Spring MVC" as default checked value
				
		//command object
				
		
		
		
		result = duoadminfunc.duoadmintest(this.duoallikeys, "AuthNATTRpt", null);
		userresult = duoadminfunc.duosearchuser(this.duoallikeys, "RetrUsers", "danielyu");
		
		for (int i = 0; i < userresult.length(); i++) {
				myModel.put("userinfo", userresult.getJSONObject(i));
		}
		
		date.setTime((long)result.getInt("mintime")*1000);
		
		myModel.put("mintime", date);
		myModel.put("adminkeys", this.duoallikeys.getAdminikeys());
		
		
		
//		return new ModelAndView("duo", "duotest", myModel);
		return new ModelAndView("duo", "duotest", myModel);
	}


//	public void setDuoadminfunc(DuoAdminFunc duoadminfunc) {
//		this.duoadminfunc = duoadminfunc;
//	}
	
}