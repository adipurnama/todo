/*
  ApplicationUserLoginValidator.java

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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rodaxsoft.todo.domain.ApplicationUser;
/**
 * ApplicationUserLoginValidator class
 */
public class ApplicationUserLoginValidator implements Validator {
	

	private static final String FIELD_REQUIRED_CODE = "field.required";
	private static final String FIELD_REQUIRED_MSG = " Field required";
	
	private StoredApplicationUserProvider storedApplicationUserProvider;
	
	public ApplicationUserLoginValidator(StoredApplicationUserProvider storedApplicationUserProvider) {
		this.storedApplicationUserProvider = storedApplicationUserProvider;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ApplicationUser.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", FIELD_REQUIRED_CODE, FIELD_REQUIRED_MSG);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", FIELD_REQUIRED_CODE, FIELD_REQUIRED_MSG);

		ApplicationUser user = (ApplicationUser) target;

		String email = user.getEmail();
		ApplicationUser storedUser = storedApplicationUserProvider.storedApplicationUserForEmail(email);
		if(null == storedUser) {
			errors.rejectValue("email", "email.not.found", "Email address not found.");
		} else {

			if(!storedUser.getEmail().equals(email)) {
				errors.rejectValue("email", "email.mismatch", "Email address mismatch.");
			}

			String rawPassword = user.getPassword();
			BCryptPasswordEncoder encoder = storedApplicationUserProvider.getBCryptPasswordEncoder();
			if(!encoder.matches(rawPassword, storedUser.getPassword())) {
				errors.rejectValue("password", "password.mismatch", "Password mismatch.");
			}
		}
	}

}
