/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */
package edu.uchicago.duo.service;

import com.duosecurity.client.Http;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import edu.uchicago.duo.domain.DuoPhone;
import edu.uchicago.duo.web.DuoEnrollController;
import java.util.List;
import java.util.Locale;
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

		request = genHttpRequest("GET", duoTokenApi);
		request.addParam("serial", tokenSerial);
		request.addParam("type", tokenType);
		request = signHttpRequest();

		try {
			jResults = (JSONArray) request.executeRequest();

			switch (attribute) {
				case "username":
					JSONArray userproperty = jResults.getJSONObject(0).getJSONArray("users");
					returnObj = userproperty.getJSONObject(0).getString("username");
					break;
			}


		} catch (Exception ex) {
			logger.error("Token Not Exist!!!");
			logger.error("The Error is: " + ex.toString());
		}

		return returnObj;
	}

	@Override
	public String getObjById() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getObjStatusById(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String createObjByParam(String param1, String param2, String param3, String param4) {
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

	@Override
	public List<DuoPhone> getAllPhones(String param1) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
