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
package edu.uchicago.duo.domain;

import edu.uchicago.duo.validator.PhoneConstraint;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class DuoPersonObj {

	//Below Using Build-in validation JSR-303
	@NotNull
	@NotEmpty
	//Below using Overwritten own validation via Annotation
	@PhoneConstraint
	String phonenumber;
	String landLineExtension;
	String username;
	String fullName;
	String email;
	String user_id;
	String phone_id;
	String QRcode;
	String choosenDevice;
	String deviceOS;
	String tabletName;
	String tokenId;
	String tokenType;
	String tokenSerial;
	/////////////////////////////////////////////
	List<DuoPhone> phones = new ArrayList<>();
	List<DuoTablet> tablets = new ArrayList<>();
	List<DuoToken> tokens = new ArrayList<>();
	/////////////////////////////////////////////

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getLandLineExtension() {
		return landLineExtension;
	}

	public void setLandLineExtension(String landLineExtension) {
		this.landLineExtension = landLineExtension;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPhone_id() {
		return phone_id;
	}

	public void setPhone_id(String phone_id) {
		this.phone_id = phone_id;
	}

	public String getQRcode() {
		return QRcode;
	}

	public void setQRcode(String QRcode) {
		this.QRcode = QRcode;
	}

	public String getChoosenDevice() {
		return choosenDevice;
	}

	public void setChoosenDevice(String choosenDevice) {
		this.choosenDevice = choosenDevice;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getTabletName() {
		return tabletName;
	}

	public void setTabletName(String tabletName) {
		this.tabletName = tabletName;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenSerial() {
		return tokenSerial;
	}

	public void setTokenSerial(String tokenSerial) {
		this.tokenSerial = tokenSerial;
	}

	public List<DuoPhone> getPhones() {
		return phones;
	}

	public void setPhones(List<DuoPhone> phones) {
		this.phones = phones;
	}

	public List<DuoTablet> getTablets() {
		return tablets;
	}

	public void setTablets(List<DuoTablet> tablets) {
		this.tablets = tablets;
	}

	public List<DuoToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<DuoToken> tokens) {
		this.tokens = tokens;
	}
}
