/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */

package edu.uchicago.duo.domain;

public class DuoAllIntegrationKeys {

	DuoISHkeys authikeys;
	DuoISHkeys adminikeys;
	DuoISHkeys verifyikeys;

	public DuoISHkeys getAuthikeys() {
		return authikeys;
	}

	public void setAuthikeys(DuoISHkeys authikeys) {
		this.authikeys = authikeys;
	}

	public DuoISHkeys getAdminikeys() {
		return adminikeys;
	}

	public void setAdminikeys(DuoISHkeys adminikeys) {
		this.adminikeys = adminikeys;
	}

	public DuoISHkeys getVerifyikeys() {
		return verifyikeys;
	}

	public void setVerifyikeys(DuoISHkeys verifyikeys) {
		this.verifyikeys = verifyikeys;
	}
}
