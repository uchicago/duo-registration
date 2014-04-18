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

package edu.uchicago.duo.validator;

import edu.uchicago.duo.domain.DuoPersonObj;
import edu.uchicago.duo.service.DuoObjInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DeviceExistDuoValidator implements Validator {

	@Autowired
	private DuoObjInterface duoPhoneService;
	@Autowired
	private DuoObjInterface duoTokenService;

	@Override
	public boolean supports(Class clazz) {
		return DuoPersonObj.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		DuoPersonObj duoPersonObj = (DuoPersonObj) target;
		String userName;
		String tokenId;

		if (duoPersonObj.getChoosenDevice().matches("mobile|landline")) {
			userName = duoPhoneService.getObjByParam(duoPersonObj.getPhonenumber(), null, "username");
			if (userName != null && userName.equals(duoPersonObj.getUsername())) {
				errors.rejectValue("phonenumber", "DeviceExistDuoValidator.alreadyReg");
			} else if (userName != null) {
				errors.rejectValue("phonenumber", "DeviceExistDuoValidator.belongSomeoneElse");
			}
		}

		if (duoPersonObj.getChoosenDevice().matches("token")) {
			userName = duoTokenService.getObjByParam(duoPersonObj.getTokenSerial(), duoPersonObj.getTokenType(), "username");
			tokenId = duoTokenService.getObjByParam(duoPersonObj.getTokenSerial(), duoPersonObj.getTokenType(), "tokenId");

			if (tokenId == null) {
				errors.rejectValue("tokenSerial", "DeviceExistDuoValidator.deviceNotInDB");
			} else if (userName != null && userName.toLowerCase().contains(duoPersonObj.getUsername())) {
				errors.rejectValue("tokenSerial", "DeviceExistDuoValidator.alreadyReg");
			} else if (userName != null) {
				errors.rejectValue("tokenSerial", "DeviceExistDuoValidator.belongSomeoneElse");
			}
		}


	}
}
