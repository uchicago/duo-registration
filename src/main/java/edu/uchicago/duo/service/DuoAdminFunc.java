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

//Trying to Kill this Class soon; Initial Brain Storm Class/Service to utilize DUO Api;
//Since Then, has been implmenting via Interface / Implementation

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
	
	public JSONObject DuoEnrollUser(DuoAllIntegrationKeys duoallkeys, String APIrequest, String userid) throws UnsupportedEncodingException, Exception {

		JSONObject result = (JSONObject) genrequestauth(duoallkeys, "POST", APIrequest, userid).executeRequest();

		return result;

	}
	
	public JSONObject DuoCreateUser(DuoAllIntegrationKeys duoallkeys, String APIrequest, String userid) throws UnsupportedEncodingException, Exception {

		JSONObject result = (JSONObject) genrequest(duoallkeys, "POST", APIrequest, userid).executeRequest();

		return result;

	}
	
	public JSONObject DuoCreatePhone(DuoAllIntegrationKeys duoallkeys, String APIrequest, String phonenumber, String type, String platform) throws UnsupportedEncodingException, Exception {

		JSONObject result = (JSONObject) genrequestphone(duoallkeys, "POST", APIrequest, phonenumber, type, platform).executeRequest();

		return result;

	}
	
	public void DuoPhone2User(DuoAllIntegrationKeys duoallkeys, String APIrequest, String userid, String phoneid) throws UnsupportedEncodingException, Exception {

		genrequestphone2usr(duoallkeys, "POST", APIrequest, userid, phoneid).executeRequest();

	}
	
	public JSONObject DuoActivateQR(DuoAllIntegrationKeys duoallkeys, String APIrequest, String phoneid) throws UnsupportedEncodingException, Exception {

		JSONObject result = (JSONObject) genReqActivation(duoallkeys, "POST", APIrequest, phoneid).executeRequest();

		return result;

	}
	
//	public void DuoPhoneFunc(DuoAllIntegrationKeys duoAllKeys, String apiRequest, String phoneID, String phoneNumber) throws UnsupportedEncodingException, Exception {
//
//		String getOrPost;
//		
//		DuoPhoneGenRequest(duoAllKeys, getOrPost, apiRequest, phoneID).executeRequest();
//
//	}
//	
//	private Http genrequestphone2usr(DuoAllIntegrationKeys duoallkeys, String getORpost, String APIrequest, String userid, String phoneid) {
//		Http request = null;
//		try {
//			request = new Http(getORpost, duoallkeys.getAdminikeys().getHostkey(), apijsontranslator(APIrequest, userid));
//
//			request.addParam("phone_id", phoneid);
//						
//			request.signRequest(duoallkeys.getAdminikeys().getIkey(), duoallkeys.getAdminikeys().getSkey());
//
//		} catch (Exception e) {
//		}
//		return request;
//	}
	
	
	
	
	private Http genReqActivation(DuoAllIntegrationKeys duoallkeys, String getORpost, String APIrequest, String phoneid) {
		Http request = null;
		try {
			request = new Http(getORpost, duoallkeys.getAdminikeys().getHostkey(), apijsontranslator(APIrequest, phoneid));

			request.signRequest(duoallkeys.getAdminikeys().getIkey(), duoallkeys.getAdminikeys().getSkey());

		} catch (Exception e) {
		}
		return request;
	}
	
	
	
	private Http genrequestphone2usr(DuoAllIntegrationKeys duoallkeys, String getORpost, String APIrequest, String userid, String phoneid) {
		Http request = null;
		try {
			request = new Http(getORpost, duoallkeys.getAdminikeys().getHostkey(), apijsontranslator(APIrequest, userid));

			request.addParam("phone_id", phoneid);
						
			request.signRequest(duoallkeys.getAdminikeys().getIkey(), duoallkeys.getAdminikeys().getSkey());

		} catch (Exception e) {
		}
		return request;
	}
	
	
	private Http genrequestphone(DuoAllIntegrationKeys duoallkeys, String getORpost, String APIrequest, String phonenumber, String type, String platform) {
		Http request = null;
		try {
			request = new Http(getORpost, duoallkeys.getAdminikeys().getHostkey(), apijsontranslator(APIrequest, null));

			request.addParam("number", phonenumber);
			request.addParam("type", type);
			request.addParam("platform", platform);
			
			request.signRequest(duoallkeys.getAdminikeys().getIkey(), duoallkeys.getAdminikeys().getSkey());

		} catch (Exception e) {
		}
		return request;
	}
	
	
	

	public JSONArray duosearchuser(DuoAllIntegrationKeys duoallkeys, String APIrequest, String userid) throws UnsupportedEncodingException, Exception {

		JSONArray result = (JSONArray) genrequest(duoallkeys, "GET", APIrequest, userid).executeRequest();

		return result;

	}
	
	private Http genrequestauth(DuoAllIntegrationKeys duoallkeys, String getORpost, String APIrequest, String userid) {
		Http request = null;
		try {
			request = new Http(getORpost, duoallkeys.getAuthikeys().getHostkey(), apijsontranslator(APIrequest, userid));

			if (userid != null && !userid.isEmpty()) {
				request.addParam("username", userid);
			}

			request.signRequest(duoallkeys.getAuthikeys().getIkey(), duoallkeys.getAuthikeys().getSkey());

		} catch (Exception e) {
		}
		return request;
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
			case "EnrollUser":
				result = "/auth/v2/enroll";
				break;
			case "CreateUser":
				result = "/admin/v1/users";
				break;
			case "CreatePhone":
				result = "/admin/v1/phones";
				break;
			case "Phone2User":
				result = "/admin/v1/users/"+userid+"/phones";
				break;
			case "ActivateQR":
				result = "/admin/v1/phones/"+userid+"/activation_url";
				break;
		}

		return result;
	}
	
//	request = null;
//			result = null;
//			request = new Http ("POST", cmd.getOptionValue("host"),"/auth/v2/enroll");
//			request.addParam("username", "dytestuser");
//			request.signRequest(cmd.getOptionValue("ikey"),	cmd.getOptionValue("skey"));
//			result = (JSONObject)request.executeRequest();
//			
//			System.out.println(result.getString("activation_barcode"));
//			System.out.println(result.getString("activation_code"));
	
	
	
	
	
}
