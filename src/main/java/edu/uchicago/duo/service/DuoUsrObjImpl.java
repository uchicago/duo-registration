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
package edu.uchicago.duo.service;

import com.duosecurity.client.Http;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import edu.uchicago.duo.domain.DuoPhone;
import edu.uchicago.duo.domain.DuoTablet;
import edu.uchicago.duo.domain.DuoToken;
import edu.uchicago.duo.web.DuoEnrollController;
import edu.uchicago.grouperabi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("duoUsrService")
public class DuoUsrObjImpl implements DuoObjInterface {

	//get log4j handler
	private static final Logger logger = Logger.getLogger(DuoEnrollController.class);
	private static final String duoUserApi = "/admin/v1/users";
	private String apiURL;
	private Http request = null;
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoAllIKeys;
	private JSONObject jResult = null;
	private JSONArray jResults = null;

	@Override
	public String createObjByParam(String userName, String fullName, String email, String na4, String na5) {
		String userId = null;

		request = genHttpRequest("POST", duoUserApi);
		request.addParam("username", userName);
		request.addParam("realname", fullName);
		request.addParam("email", email);
		request = signHttpRequest();

		try {
			jResult = (JSONObject) request.executeRequest();
			userId = jResult.getString("user_id");
		} catch (Exception ex) {
		}

		return userId;

	}

	@Override
	public String getObjByParam(String userName, String na1, String attribute) {
		String returnObj = null;

		request = genHttpRequest("GET", duoUserApi);
		request.addParam("username", userName);
		request = signHttpRequest();

		try {
			jResults = (JSONArray) request.executeRequest();


			switch (attribute) {
				case "userId":
					returnObj = jResults.getJSONObject(0).getString("user_id");
					;
					break;
			}


		} catch (Exception ex) {
			logger.debug("2FA Debug - "+"User Not Exist!!!If triggered by Validation is a good thing, not error");
			logger.debug("2FA Debug - "+"The Error is: " + ex.toString());
		}

		return returnObj;

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private Http genHttpRequest(String getOrPost, String apiURL) {
		request = null;
		try {
			request = new Http(getOrPost, duoAllIKeys.getAdminikeys().getHostkey(), apiURL);

		} catch (Exception e) {
		}

		return request;
	}

	private Http signHttpRequest() {

		try {
			request.signRequest(duoAllIKeys.getAdminikeys().getIkey(), duoAllIKeys.getAdminikeys().getSkey());

		} catch (Exception e) {
		}

		return request;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String getObjById() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getObjStatusById(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void associateObjs(String param1, String param2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<DuoPhone> getAllPhones(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<DuoTablet> getAllTablets(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<DuoToken> getAllTokens(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void deleteObj(String param1, String param2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void resyncObj(String param1, String param2, String param3, String param4) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, Object> verifyObj(String param1, String param2, String param3) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String objActionById(String id, String action) {

		Group targetgroup = null;
		Boolean memberExist = false;
		String result = null;
		GrouperQueryEngine gqe;
		ArrayList<Group> groups;

		switch (action) {
			case "SearchDuoForce":
			case "AddUserToDuoForce":
			case "RemoveUserFromDuoForce":
				gqe = new GrouperQueryEngine("uc:applications:shibboleth:MCB");
				groups = gqe.getStem().getGroups();
				for (Group aGroup : groups) {
					if (aGroup.getName().endsWith("force-duo")) {
						targetgroup = aGroup;
					}
				}
				break;
			case "SearchRegistered":
			case "AddUserToRegistered":
			case "RemoveUserFromRegistered":
				gqe = new GrouperQueryEngine("uc:applications:2fa");
				groups = gqe.getStem().getGroups();
				for (Group aGroup : groups) {
					if (aGroup.getName().endsWith("registered")) {
						targetgroup = aGroup;
					}
				}
				break;
		}



//		GrouperQueryEngine gqe = new GrouperQueryEngine("uc:applications:shibboleth:MCB");
//		ArrayList<Group> groups = gqe.getStem().getGroups();
//		for (Group aGroup : groups) {
//			if (aGroup.getName().endsWith("force-duo")) {
//				forceDuo = aGroup;
//			}
//		}

		switch (action) {
			case "SearchDuoForce":
			case "SearchRegistered":
				try {
					memberExist = targetgroup.hasMember(new Subject(id));
				} catch (GrouperABIException ex) {
				}
				if (memberExist) {
					result = "Y";
				} else {
					result = "N";
				}
				break;
			case "AddUserToDuoForce":
			case "AddUserToRegistered":
				result = "N";
				try {
					targetgroup.addMember(new Subject(id), new Subject(id));
					result = "Y";
				} catch (GrouperABIException ex) {
				}
				break;
			case "RemoveUserFromDuoForce":
			case "RemoveUserFromRegistered":
				result = "N";
				try {
					targetgroup.removeMember(new Subject(id), new Subject(id));
					result = "Y";
				} catch (GrouperABIException ex) {
				}
				break;

		}

		return result;
	}
}
