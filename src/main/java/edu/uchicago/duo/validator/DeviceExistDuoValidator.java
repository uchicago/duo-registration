/**
 * @author Daniel Yu, danielyu@uchicago.edu
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
		
		if (duoPersonObj.getChoosenDevice().matches("mobile|landline")) {
			userName = duoPhoneService.getObjByParam(duoPersonObj.getPhonenumber(), null, "username");
			if (userName != null && userName.equals(duoPersonObj.getUsername())) {
				errors.rejectValue("phonenumber", "DeviceExistDuoValidator.alreadyReg");
			}else if (userName != null) {
				errors.rejectValue("phonenumber", "DeviceExistDuoValidator.belongSomeoneElse");
			}
		}
		
		if (duoPersonObj.getChoosenDevice().matches("token")) {
			userName = duoTokenService.getObjByParam(duoPersonObj.getTokenSerial(), duoPersonObj.getTokenType(), "username");
			if (userName != null && userName.equals(duoPersonObj.getUsername())) {
				errors.rejectValue("tokenSerial", "DeviceExistDuoValidator.alreadyReg");
			}else if (userName != null) {
				errors.rejectValue("tokenSerial", "DeviceExistDuoValidator.belongSomeoneElse");
			}else if (userName == null) {
				errors.rejectValue("tokenSerial", "DeviceExistDuoValidator.deviceNotInDB");
			}
		}
		
		
	}
}
