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

@Service("duoTabletService")
public class DuoTabletObjImpl implements DuoObjInterface {
	//get log4j handler

	private static final Logger logger = Logger.getLogger(DuoEnrollController.class);
	private static final String duoPhoneApi = "/admin/v1/phones";
	private static final String duoUserApi = "/admin/v1/users";
	private String apiURL;
	private Http request = null;
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoAllIKeys;
	private JSONObject jResult = null;
	private JSONArray jResults = null;
	@Autowired
	private MessageSource message;

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
	public List<DuoTablet> getAllTablets(String userId) {
		apiURL = new String();
		apiURL = duoUserApi + "/" + userId;
		request = genHttpRequest("GET", apiURL);
		request = signHttpRequest();

		jResults = null;

		DuoTablet duoTablet;
		JSONArray jTablets;
		List<DuoTablet> tablets = new ArrayList<>();
		String phoneNumber;
		int counter = 0;


		try {
			jResult = (JSONObject) request.executeRequest();
			jTablets = jResult.getJSONArray("phones");
			
			for (int t = 0; t < jTablets.length(); t++) {
				phoneNumber = jTablets.getJSONObject(t).getString("number");
				if (phoneNumber == null || phoneNumber.isEmpty()) {

					duoTablet = new DuoTablet();
					duoTablet.setId(jTablets.getJSONObject(t).getString("phone_id"));
					duoTablet.setDeviceName(jTablets.getJSONObject(t).getString("name"));
					duoTablet.setPlatform(jTablets.getJSONObject(t).getString("platform"));
					duoTablet.setType(jTablets.getJSONObject(t).getString("type"));
					duoTablet.setActivationStatus(jTablets.getJSONObject(t).getBoolean("activated"));
					duoTablet.setSmsPassCodeSent(jTablets.getJSONObject(t).getBoolean("sms_passcodes_sent"));
					duoTablet.setActivationStatus(jTablets.getJSONObject(t).getBoolean("activated"));
					duoTablet.setSmsPassCodeSent(jTablets.getJSONObject(t).getBoolean("sms_passcodes_sent"));

					String capabilities = jTablets.getJSONObject(t).getJSONArray("capabilities").toString();
					if (capabilities.toLowerCase().contains("push")) {
						duoTablet.setCapablePush(true);
					}
					if (capabilities.toLowerCase().contains("sms")) {
						duoTablet.setCapableSMS(true);
					}
					if (capabilities.toLowerCase().contains("phone")) {
						duoTablet.setCapablePhone(true);
					}

					tablets.add(duoTablet);
					counter++;
				}

			}

		} catch (Exception ex) {
			logger.error("2FA Error - "+"Unable to Excute Method 'GetAllPhones'");
			logger.error("2FA Error - "+"The Error is(TabletObjImp): " + ex.toString());
		}
		
		logger.debug("2FA Debug - "+"Total Number of Tablets(TabletObjImp) " + userId + " has:" + counter);
		return tablets;


	}
	
	@Override
	public void deleteObj(String tabletId, String na) {
		apiURL = new String();
		apiURL = duoPhoneApi + "/" + tabletId;

		request = genHttpRequest("DELETE", apiURL);
		request = signHttpRequest();

		try {
			request.executeRequest();
			logger.debug("2FA Debug - "+"Successfully Deleted Tablet, ID="+tabletId);
		} catch (Exception ex) {
			logger.error("2FA Error - "+"Unable to Delete Tablet from Useraccount!!!");
			logger.error("2FA Error - "+"The Error is(TabletObjImp): " + ex.toString());
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
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
	public String getObjByParam(String param1, String param2, String attribute) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String createObjByParam(String param1, String param2, String param3, String param4, String param5) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void associateObjs(String param1, String param2) {
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
	public List<DuoToken> getAllTokens(String param1) {
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
