/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */

package edu.uchicago.duo.service;

import com.duosecurity.client.Http;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("duoPhoneService")
public class DuoPhoneObjImpl implements DuoObjInterface {

	private static final String duoPhoneApi = "/admin/v1/phones";
	private String apiURL;
	private Http request = null;
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoAllIKeys;
	private JSONObject jResult = null;
	private JSONArray jResults = null;
	@Autowired
	private MessageSource message;

	@Override
	public String getObjByParam(String phoneNumber, String extension) {
		String userName = null;

		request = genHttpRequest("GET", duoPhoneApi);
		request.addParam("number", phoneNumber);
		request = signHttpRequest();

		try {
			jResults = (JSONArray) request.executeRequest();
			if (jResults.length() > 0) {
				JSONArray userproperty = jResults.getJSONObject(0).getJSONArray("users");
				userName = userproperty.getJSONObject(0).getString("username");
			}

		} catch (Exception ex) {
			///Need To Code To cATCH eXCEPTION 400 For invalid phone number
		}

		return userName;

	}

	@Override
	public String createObjByParam(String phoneNumber, String device, String deviceOS) {
		String phoneID = null;

		request = genHttpRequest("POST", duoPhoneApi);
		if (device.equals("mobile")) {
			request.addParam("number", phoneNumber);
			request.addParam("type", device);
			request.addParam("platform", deviceOS);
		}
		
		if (device.equals("tablet")) {
			request.addParam("type", "mobile");
			request.addParam("platform", deviceOS);
		}
		
		
		request = signHttpRequest();

		try {
			jResult = (JSONObject) request.executeRequest();
			phoneID = jResult.getString("phone_id");
		} catch (Exception ex) {
		}

		return phoneID;

	}

	@Override
	public String getObjStatusById(String phoneId) {
		String status = "false";
		apiURL = new String();
		jResult = null;

		apiURL = duoPhoneApi + '/' + phoneId;
		request = genHttpRequest("GET", apiURL);
		request = signHttpRequest();

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

		request = genHttpRequest("POST", apiURL);

		switch (action) {
			case "activationSMS":
				request.addParam("activation_msg", message.getMessage("SMS.Device.Activation", null, Locale.getDefault()));
				break;
		}

		request = signHttpRequest();

		try {
			jResult = (JSONObject) request.executeRequest();

			switch (action) {
				case "qrCode":
					actionResult = jResult.getString("activation_barcode");
					break;
			}
		} catch (Exception ex) {
		}

		return actionResult;

	}

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
	public void associateObjs(String param1, String param2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
