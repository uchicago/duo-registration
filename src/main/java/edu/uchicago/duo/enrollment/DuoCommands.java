package edu.uchicago.duo.enrollment;

/**
 *
 * @author danielyu
 */
public class DuoCommands {

	DuoISHKeysold authikeys;
	DuoISHKeysold adminikeys;
	DuoISHKeysold verifyikeys;

	public DuoISHKeysold getAuthikeys() {
		return authikeys;
	}

	public void setAuthikeys(DuoISHKeysold authikeys) {
		this.authikeys = authikeys;
	}

	public DuoISHKeysold getAdminikeys() {
		return adminikeys;
	}

	public void setAdminikeys(DuoISHKeysold adminikeys) {
		this.adminikeys = adminikeys;
	}

	public DuoISHKeysold getVerifyikeys() {
		return verifyikeys;
	}

	public void setVerifyikeys(DuoISHKeysold verifyikeys) {
		this.verifyikeys = verifyikeys;
	}
}
