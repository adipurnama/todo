/*
  ApplicationUserValidator.java

  The MIT License (MIT)

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  Permission is hereby granted, free of charge, to any person obtaining a copy 
  of this software and associated documentation files (the "Software"), to deal 
  in the Software without restriction, including without limitation the rights 
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
  copies of the Software, and to permit persons to whom the Software is 
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all 
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
  SOFTWARE.
*/
package com.rodaxsoft.todo.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rodaxsoft.todo.domain.ApplicationUser;

/**
 * ApplicationUserSignupValidator class
 *
 */
public class ApplicationUserSignupValidator implements Validator {
	
	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
	/**
	 * Password Regex string.
	 * Password (at least 8 characters, including 1 uppercase letter, 1 lowercase letter, and 1 number)
	 */
	public static final String PASSWORD_REGEX = "(?=^.{8,}$)(?=.*\\d)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
	private static final RegexValidator PASSWORD_VALIDATOR = new RegexValidator(PASSWORD_REGEX);


	@Override
	public boolean supports(Class<?> clazz) {
		return ApplicationUser.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
		
		ApplicationUser user = (ApplicationUser) target;
		if(!EMAIL_VALIDATOR.isValid(user.getEmail())) {
			errors.rejectValue("email", "email.invalid");			
		}
		
		if(!PASSWORD_VALIDATOR.isValid(user.getPassword())) {
			errors.rejectValue("password", "password.invalid", "Password must have at least 8 characters, including 1 uppercase letter, 1 lowercase letter, and 1 number.");
		}
		
	}

}
