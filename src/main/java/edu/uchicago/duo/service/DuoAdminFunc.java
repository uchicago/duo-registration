package edu.uchicago.duo.service;

import com.duosecurity.client.Http;
import edu.uchicago.duo.domain.DuoAllIntegrationKeys;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class DuoAdminFunc {

	public JSONObject duoadmintest(DuoAllIntegrationKeys duoallkeys, String APIrequest, String userid) throws UnsupportedEncodingException, Exception {

		JSONObject result = (JSONObject) genrequest(duoallkeys, "GET", APIrequest, userid).executeRequest();

		return result;

	}

	public JSONArray duosearchuser(DuoAllIntegrationKeys duoallkeys, String APIrequest, String userid) throws UnsupportedEncodingException, Exception {

		JSONArray result = (JSONArray) genrequest(duoallkeys, "GET", APIrequest, userid).executeRequest();

		return result;

	}

	private Http genrequest(DuoAllIntegrationKeys duoallkeys, String getORpost, String APIrequest, String userid) {
		Http request = null;
		try {
			request = new Http(getORpost, duoallkeys.getAdminikeys().getHostkey(), apijsontranslator(APIrequest, userid));

			if (userid != null && !userid.isEmpty()) {
				request.addParam("username", userid);
			}

			request.signRequest(duoallkeys.getAdminikeys().getIkey(), duoallkeys.getAdminikeys().getSkey());

		} catch (Exception e) {
		}
		return request;
	}

	private static String apijsontranslator(String APIrequest, String userid) {
		String result = null;

		switch (APIrequest) {
			case "RetrUsers":
				result = "/admin/v1/users";
				break;
			case "AuthNATTRpt":
				result = "/admin/v1/info/authentication_attempts";
				break;
		}

		return result;
	}
}
