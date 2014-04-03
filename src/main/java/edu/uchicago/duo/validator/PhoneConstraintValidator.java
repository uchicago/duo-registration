/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */

package edu.uchicago.duo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<PhoneConstraint, String> {

	
	@Override
	public void initialize(PhoneConstraint String) { }

	@Override
	public boolean isValid(String phoneField, ConstraintValidatorContext cxt) {
		if(phoneField == null) {
			return false;
		}
		return phoneField.matches("[0-9()-]*");
	}

}
