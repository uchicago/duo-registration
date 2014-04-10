/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */
package edu.uchicago.duo.service;

import com.duosecurity.client.Http;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import edu.uchicago.duo.domain.DuoPhone;
import edu.uchicago.duo.web.DuoEnrollController;
import java.util.List;
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
	public String createObjByParam(String userName, String fullName, String email, String na3) {
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

//	@Override
//	public void associateObjs(String userId, String deviceId, String choosenDevice) {
//		apiURL = new String();
//
//		switch (choosenDevice) {
//			case "mobile":
//				apiURL = duoUserApi + "/" + userId + "/phones";
//				request = genHttpRequest("POST", apiURL);
//				request.addParam("phone_id", deviceId);
//				break;
//			case "tablet":
//				apiURL = duoUserApi + "/" + userId + "/phones";
//				request = genHttpRequest("POST", apiURL);
//				request.addParam("phone_id", deviceId);
//				break;
//			case "token":
//				apiURL = duoUserApi + "/" + userId + "/tokens";
//				request = genHttpRequest("POST", apiURL);
//				request.addParam("token_id", deviceId);
//				break;
//		}
//
//
////		apiURL = duoUserApi + "/" + userId + "/phones";
////		request = genHttpRequest("POST", apiURL);
////		request.addParam("phone_id", phoneId);
//		request = signHttpRequest();
//
//		try {
//			request.executeRequest();
//		} catch (Exception ex) {
//			logger.error("Unable to Link " +choosenDevice+ " to User account!!!!");
//		}
//		logger.info("Successfully Linked "+choosenDevice+ " to User account");
//	}

	@Override
	public String getObjStatusById(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
	public String objActionById(String param1, String param2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getObjById() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
					returnObj = jResults.getJSONObject(0).getString("user_id");;
					break;
			}


		} catch (Exception ex) {
			logger.error("User Not Exist!!!If triggered by Validation is a good thing, not error");
			logger.error("The Error is: " + ex.toString());
		}

		return returnObj;

	}

	@Override
	public void associateObjs(String param1, String param2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<DuoPhone> getAllPhones(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
