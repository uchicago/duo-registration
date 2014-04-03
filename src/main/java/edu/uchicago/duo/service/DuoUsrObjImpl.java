/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */

package edu.uchicago.duo.service;

import com.duosecurity.client.Http;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("duoUsrService")
public class DuoUsrObjImpl implements DuoObjInterface {

	private static final String duoUserApi = "/admin/v1/users";
	private String apiURL;
	private Http request = null;
	@Autowired(required = true)
	private DuoAllIntegrationKeys duoAllIKeys;
	private JSONObject jResult = null;
	private JSONArray userresult = null;

	@Override
	public String createObjByParam(String userName, String na1, String na2) {
		String userId = null;
		
		request = genHttpRequest("POST", duoUserApi);
		request.addParam("username", userName);
		request = signHttpRequest();

		try {
			jResult = (JSONObject) request.executeRequest();
			userId = jResult.getString("user_id");
		} catch (Exception ex) {
		}

		return userId;

	}

	@Override
	public void associateObjs(String userID, String phoneID) {
		apiURL = new String();
		
		apiURL = duoUserApi + "/" + userID + "/phones";
		request = genHttpRequest("POST", apiURL);
		request.addParam("phone_id", phoneID);
		request = signHttpRequest();

		try {
			request.executeRequest();
		} catch (Exception ex) {
		}

	}
	
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
	public String getObjByParam(String param1, String param2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
}
