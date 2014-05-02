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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("duoPhoneService")
public class DuoPhoneObjImpl implements DuoObjInterface {

	//get log4j handler
	private static final Logger logger = Logger.getLogger(DuoEnrollController.class);
	private static final String duoPhoneApi = "/admin/v1/phones";
	private static final String duoUserApi = "/admin/v1/users";
	private static final String duoVerifyApi = "/verify/v1";
	private String apiURL;
	private Http request = null;
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoAllIKeys;
	private JSONObject jResult = null;
	private JSONArray jResults = null;
	@Autowired
	private MessageSource message;

	@Override
	public String getObjByParam(String phoneNumber, String landLineExtension, String attribute) {
		String returnObj = null;

		request = genHttpRequest("GET", duoPhoneApi, "admin");
		request.addParam("number", phoneNumber);
		if (StringUtils.hasLength(landLineExtension)) {
			request.addParam("extension", landLineExtension);
		}
		request = signHttpRequest("admin");

		try {
			jResults = (JSONArray) request.executeRequest();


			switch (attribute) {
				case "username":
					JSONArray userproperty = jResults.getJSONObject(0).getJSONArray("users");
					returnObj = userproperty.getJSONObject(0).getString("username");
					break;
			}


		} catch (Exception ex) {
			logger.error("Phone Not Exist!!!If triggered by Validation is a good thing, not error");
			logger.error("The Error is: " + ex.toString());
		}

		return returnObj;

	}

	@Override
	public String createObjByParam(String phoneNumber, String device, String deviceOS, String tabletName, String landLineExtension) {
		String phoneID = null;

		request = genHttpRequest("POST", duoPhoneApi, "admin");
		if (device.equals("mobile")) {
			request.addParam("number", phoneNumber);
			request.addParam("type", device);
			request.addParam("platform", deviceOS);
		}

		if (device.equals("tablet")) {
			request.addParam("type", "mobile");
			request.addParam("platform", deviceOS);
			request.addParam("name", tabletName);
		}

		if (device.equals("landline")) {
			request.addParam("number", phoneNumber);
			request.addParam("type", "landline");
			if (StringUtils.hasLength(landLineExtension)) {
				request.addParam("extension", landLineExtension);
			}
		}


		request = signHttpRequest("admin");

		try {
			jResult = (JSONObject) request.executeRequest();
			phoneID = jResult.getString("phone_id");
			logger.info("Successfully Created Phone, Type:" + device + "/Number:" + phoneNumber);
		} catch (Exception ex) {
			logger.error("Unable to Create Duo Phone Object!!!!, Number:" + phoneNumber);
			logger.error("The Error is(PhoneObjImp): " + ex.toString());
		}

		return phoneID;

	}

	@Override
	@Async
	public String getObjStatusById(String phoneId) {
		String status = "false";
		apiURL = new String();
		jResult = null;

		apiURL = duoPhoneApi + '/' + phoneId;
		request = genHttpRequest("GET", apiURL, "admin");
		request = signHttpRequest("admin");

		try {
			jResult = (JSONObject) request.executeRequest();

			if (jResult.getBoolean("activated")) {
				status = "true";
			}
		} catch (Exception ex) {
		}

		return status;
	}

	@Override
	public void associateObjs(String userId, String phoneId) {
		apiURL = new String();
		apiURL = duoUserApi + "/" + userId + "/phones";

		request = genHttpRequest("POST", apiURL, "admin");
		request.addParam("phone_id", phoneId);
		request = signHttpRequest("admin");

		try {
			request.executeRequest();
			logger.info("Successfully Linked Phone/Tablet to User account");
		} catch (Exception ex) {
			logger.error("Unable to Link Phone/Tablet to User account!!!!");
			logger.error("The Error is(PhoneObjImp): " + ex.toString());
		}

	}

	@Override
	public String objActionById(String phoneId, String action) {
		String actionResult = null;
		apiURL = new String();
		jResult = null;

		switch (action) {
			case "qrCode":
				apiURL = duoPhoneApi + "/" + phoneId + "/activation_url";
				break;
			case "activationSMS":
				apiURL = duoPhoneApi + "/" + phoneId + "/send_sms_activation";
				break;
			case "installUrlSMS":
				apiURL = duoPhoneApi + "/" + phoneId + "/send_sms_installation";
				break;
			case "passcodeSMS":
				apiURL = duoPhoneApi + "/" + phoneId + "/send_sms_passcodes";
				break;
		}

		request = genHttpRequest("POST", apiURL, "admin");

		switch (action) {
			case "activationSMS":
				request.addParam("activation_msg", message.getMessage("SMS.Device.Activation", null, Locale.getDefault()));
				break;
		}

		request = signHttpRequest("admin");

		try {

			switch (action) {
				case "activationSMS":
					jResult = (JSONObject) request.executeRequest();
					actionResult = null;
					logger.info("Activation SMS sent");
					break;
				case "qrCode":
					jResult = (JSONObject) request.executeRequest();
					actionResult = jResult.getString("activation_barcode");
					logger.info("Activation QR Code generated");
					break;
				case "passcodeSMS":
					actionResult = (String) request.executeRequest();
					logger.info("SMS Passcode Sent Successfully");
					break;
			}
		} catch (Exception ex) {
			logger.error("Object Action Failed for: " + action);
			logger.error("The Error is(PhoneObjImp): " + ex.toString());
		}

		return actionResult;

	}

	/**
	 * Why use UserId instead of UserName??
	 *
	 * 1)UserId always return SINGLE record, although Username search should
	 * only have one record also...
	 *
	 * 2)The JSON Response code for userID search is either success or User not
	 * found, easier to capture the exception?
	 *
	 * 3)Return a JSON object instead of JSON Array, safe one layer of parsing
	 */
	@Override
	public List<DuoPhone> getAllPhones(String userId) {
		apiURL = new String();
		apiURL = duoUserApi + "/" + userId;
		request = genHttpRequest("GET", apiURL, "admin");
		request = signHttpRequest("admin");

		jResults = null;

		DuoPhone duoPhone;
		JSONArray jPhones;
		List<DuoPhone> phones = new ArrayList<>();
		String phoneNumber;
		int counter = 0;


		try {
			jResult = (JSONObject) request.executeRequest();
			jPhones = jResult.getJSONArray("phones");

			for (int p = 0; p < jPhones.length(); p++) {
				phoneNumber = jPhones.getJSONObject(p).getString("number");
				if (phoneNumber != null && !phoneNumber.isEmpty()) {

					duoPhone = new DuoPhone();
					duoPhone.setId(jPhones.getJSONObject(p).getString("phone_id"));
					duoPhone.setPhoneNumber(jPhones.getJSONObject(p).getString("number"));
					duoPhone.setPlatform(jPhones.getJSONObject(p).getString("platform"));
					duoPhone.setType(jPhones.getJSONObject(p).getString("type"));
					duoPhone.setActivationStatus(jPhones.getJSONObject(p).getBoolean("activated"));
					duoPhone.setSmsPassCodeSent(jPhones.getJSONObject(p).getBoolean("sms_passcodes_sent"));

					String capabilities = jPhones.getJSONObject(p).getJSONArray("capabilities").toString();
					if (capabilities.toLowerCase().contains("push")) {
						duoPhone.setCapablePush(true);
					}
					if (capabilities.toLowerCase().contains("sms")) {
						duoPhone.setCapableSMS(true);
					}
					if (capabilities.toLowerCase().contains("phone")) {
						duoPhone.setCapablePhone(true);
					}

					phones.add(duoPhone);
					counter++;
				}

			}

		} catch (Exception ex) {
			logger.error("Unable to Excute Method 'GetAllPhones'");
			logger.error("The Error is(PhoneObjImp): " + ex.toString());
		}

		logger.info("Total Number of Phones(DuoPhoneImp) " + userId + " has:" + counter);
		return phones;

	}

	@Override
	public void deleteObj(String phoneId, String na) {
		apiURL = new String();
		apiURL = duoPhoneApi + "/" + phoneId;

		request = genHttpRequest("DELETE", apiURL, "admin");
		request = signHttpRequest("admin");

		try {
			request.executeRequest();
			logger.info("Successfully Deleted phone, ID=" + phoneId);
		} catch (Exception ex) {
			logger.error("Unable to Delete Phone from Useraccount!!!");
			logger.error("The Error is(PhoneObjImp): " + ex.toString());
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	private Http genHttpRequest(String getOrPost, String apiURL, String apiType) {
		request = null;
		try {
			switch (apiType) {
				case "admin":
					request = new Http(getOrPost, duoAllIKeys.getAdminikeys().getHostkey(), apiURL);
					break;
				case "verify":
					request = new Http(getOrPost, duoAllIKeys.getVerifyikeys().getHostkey(), apiURL);
					break;
			}
		} catch (Exception e) {
		}

		return request;
	}

	private Http signHttpRequest(String apiType) {

		try {
			switch (apiType) {
				case "admin":
					request.signRequest(duoAllIKeys.getAdminikeys().getIkey(), duoAllIKeys.getAdminikeys().getSkey());
					break;
				case "verify":
					request.signRequest(duoAllIKeys.getVerifyikeys().getIkey(), duoAllIKeys.getVerifyikeys().getSkey());
					break;
			}
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
	public List<DuoTablet> getAllTablets(String param1) {
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	@Async
	public Map<String, Object> verifyObj(String Number, String action) {
		String txid = null;
		String pin = null;
		String info = null;
		String state = null;

		jResult = null;

		Map<String, Object> verifyInfo = new HashMap<>();

		apiURL = new String();
		apiURL = duoVerifyApi + "/" + action;

		switch (action) {
			case "call":
				request = genHttpRequest("POST", apiURL, "verify");
				request.addParam("phone", Number);
				request.addParam("message", message.getMessage("CALL.Device.Verify", null, Locale.getDefault()));
				break;
			case "status":
				request = genHttpRequest("GET", apiURL, "verify");
				request.addParam("txid", Number);
				break;

		}

		request = signHttpRequest("verify");

		try {
			jResult = (JSONObject) request.executeRequest();

			switch (action) {
				case "call":
					txid = jResult.getString("txid");
					pin = jResult.getString("pin");
					verifyInfo.put("txid", txid);
					verifyInfo.put("pin", pin);
					logger.info("txid=" + txid + ",Pin=" + pin);
					break;
				case "status":
					info = jResult.getString("info");
					state = jResult.getString("state");
					verifyInfo.put("info", info);
					verifyInfo.put("state", state);
					break;
			}

		} catch (Exception ex) {
			logger.error("Unable to Call Phone!!!");
			logger.error("The Error is(PhoneObjImp): " + ex.toString());
		}

		return verifyInfo;

	}
}
