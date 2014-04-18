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

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DuoTablet {
	private String id;
	private String deviceName;
	private String platform;
	private String type;
	private int usersize;
	private boolean activationStatus;
	private boolean capableSMS = false;
	private boolean capablePush = false;
	private boolean capablePhone = false;
	private boolean smsPassCodeSent = false;
	private List<String> users;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUsersize() {
		return usersize;
	}

	public void setUsersize(int usersize) {
		this.usersize = usersize;
	}

	public boolean isActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(boolean activationStatus) {
		this.activationStatus = activationStatus;
	}

	public boolean isCapableSMS() {
		return capableSMS;
	}

	public void setCapableSMS(boolean capableSMS) {
		this.capableSMS = capableSMS;
	}

	public boolean isCapablePush() {
		return capablePush;
	}

	public void setCapablePush(boolean capablePush) {
		this.capablePush = capablePush;
	}

	public boolean isCapablePhone() {
		return capablePhone;
	}

	public void setCapablePhone(boolean capablePhone) {
		this.capablePhone = capablePhone;
	}

	public boolean isSmsPassCodeSent() {
		return smsPassCodeSent;
	}

	public void setSmsPassCodeSent(boolean smsPassCodeSent) {
		this.smsPassCodeSent = smsPassCodeSent;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	
}
