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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service("duoTokenService")
public class DuoTokenObjImpl implements DuoObjInterface {

	//get log4j handler
	private static final Logger logger = Logger.getLogger(DuoEnrollController.class);
	private static final String duoTokenApi = "/admin/v1/tokens";
	private static final String duoUserApi = "/admin/v1/users";
	private String apiURL;
	private Http request = null;
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoAllIKeys;
	private JSONObject jResult = null;
	private JSONArray jResults = null;
	@Autowired
	private MessageSource message;

	@Override
	public String getObjByParam(String tokenSerial, String tokenType, String attribute) {
		String returnObj = null;
		JSONArray users;

		request = genHttpRequest("GET", duoTokenApi);
		request.addParam("serial", tokenSerial);
		request.addParam("type", tokenType);
		request = signHttpRequest();

		try {
			jResults = (JSONArray) request.executeRequest();

			switch (attribute) {
				case "username":
					if (jResults.getJSONObject(0).getJSONArray("users").length() > 0) {
						users = jResults.getJSONObject(0).getJSONArray("users");
						returnObj = "|";
						for (int i = 0; i < users.length(); i++) {
							returnObj += users.getJSONObject(i).getString("username") + "|";
						}
						logger.debug("2FA Debug - "+"Token's Users Retrieved:" + returnObj);
					}
					break;
				case "tokenId":
					returnObj = jResults.getJSONObject(0).getString("token_id");
					logger.debug("2FA Debug - "+"Token's ID is:" + returnObj);
					break;
			}
		} catch (Exception ex) {
			if (jResults.length() == 0) {
				logger.error("2FA Error - "+"Token Not Exist!!!");
			} else {
				logger.error("2FA Error - "+"Token Search Encounter a problem!!!!");
			}
			logger.error("2FA Error - "+"The Error is: " + ex.toString());
		}

		return returnObj;
	}

	@Override
	public void associateObjs(String userId, String tokenId) {

		apiURL = new String();
		apiURL = duoUserApi + "/" + userId + "/tokens";

		request = genHttpRequest("POST", apiURL);
		request.addParam("token_id", tokenId);
		request = signHttpRequest();

		try {
			request.executeRequest();
			logger.debug("2FA Debug - "+"Successfully Linked Token to User account");
		} catch (Exception ex) {
			logger.error("2FA Error - "+"Unable to Link Token to User account!!!!");
			logger.error("2FA Error - "+"The Error is(TokenObjImp): " + ex.toString());
		}

	}

	/**
	 * Why use UserId instead of UserName??
	 *
	 * 1)UserId always SINGLE record, although Username search should only have
	 * one record also...
	 *
	 * 2)The JSON Response code for userID search is either success or User not
	 * found, easier to capture the exception?
	 *
	 * 3)Return a JSON object instead of JSON Array, safe one layer of parsing
	 */
	@Override
	public List<DuoToken> getAllTokens(String userId) {
		apiURL = new String();
		apiURL = duoUserApi + "/" + userId;
		request = genHttpRequest("GET", apiURL);
		request = signHttpRequest();

		jResults = null;

		DuoToken duoToken;
		JSONArray jTokens;
		List<DuoToken> tokens = new ArrayList<>();

		try {
			jResult = (JSONObject) request.executeRequest();
			jTokens = jResult.getJSONArray("tokens");

			logger.debug("2FA Debug - "+"Total Number of Tokens(DuoTokenImp) " + userId + " has:" + jTokens.length());

			for (int t = 0; t < jTokens.length(); t++) {

				duoToken = new DuoToken();
				duoToken.setId(jTokens.getJSONObject(t).getString("token_id"));
				duoToken.setType(jTokens.getJSONObject(t).getString("type"));
				duoToken.setSerial(jTokens.getJSONObject(t).getString("serial"));

				tokens.add(duoToken);
			}

		} catch (Exception ex) {
			logger.error("2FA Error - "+"Unable to Excute Method 'GetAllTokens'");
			logger.error("2FA Error - "+"The Error is(PhoneObjImp): " + ex.toString());
		}

		return tokens;

	}

	@Override
	public void deleteObj(String tokenId, String userId) {
		apiURL = new String();
		apiURL = duoUserApi + "/" + userId + "/tokens/" + tokenId;

		request = genHttpRequest("DELETE", apiURL);
		request = signHttpRequest();

		try {
			request.executeRequest();
			logger.debug("2FA Debug - "+"Successfully Disassociate Token, ID=" + tokenId);
		} catch (Exception ex) {
			logger.error("2FA Error - "+"Unable to Disassociate Token from Useraccount!!!");
			logger.error("2FA Error - "+"The Error is(TokenObjImp): " + ex.toString());
		}
	}

	@Override
	public void resyncObj(String tokenId, String resyncCode1, String resyncCode2, String resyncCode3) {
		apiURL = new String();
		apiURL = duoTokenApi + "/" + tokenId + "/resync";

		request = genHttpRequest("POST", apiURL);
		request.addParam("code1", resyncCode1);
		request.addParam("code2", resyncCode2);
		request.addParam("code3", resyncCode3);		
		request = signHttpRequest();

		try {
			request.executeRequest();
			logger.debug("2FA Debug - "+"Successfully RESYNC Token to User account");
		} catch (Exception ex) {
			logger.error("2FA Error - "+"Unable to RESYNC Token for User account!!!!");
			logger.error("2FA Error - "+"The Error is(TokenObjImp): " + ex.toString());
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	@Override
	public String getObjById() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getObjStatusById(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String createObjByParam(String param1, String param2, String param3, String param4, String param5) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String objActionById(String param1, String param2) {
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
	public Map<String, Object> verifyObj(String param1, String param2, String param3) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
