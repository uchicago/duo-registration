/**
 * @author Daniel Yu, danielyu@uchicago.edu
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
	String username;
	String fullName;
	String email;
	String user_id;
	String phone_id;
	String QRcode;
	String choosenDevice;
	String deviceOS;
	String tokenId;
	String tokenType;
	String tokenSerial;
	String tabletName;
	
	List<DuoPhone> phones = new ArrayList<>();

	public List<DuoPhone> getPhones() {
		return phones;
	}

	public void setPhones(List<DuoPhone> phones) {
		this.phones = phones;
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

	public String getQRcode() {
		return QRcode;
	}

	public void setQRcode(String QRcode) {
		this.QRcode = QRcode;
	}

	public String getPhone_id() {
		return phone_id;
	}

	public void setPhone_id(String phone_id) {
		this.phone_id = phone_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getChoosenDevice() {
		return choosenDevice;
	}

	public void setChoosenDevice(String choosenDevice) {
		this.choosenDevice = choosenDevice;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
}
