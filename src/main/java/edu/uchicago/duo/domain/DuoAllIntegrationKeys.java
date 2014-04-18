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
