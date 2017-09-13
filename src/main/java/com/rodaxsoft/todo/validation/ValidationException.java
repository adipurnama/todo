/*
  ValidationException.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.validation;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * ValidationException class
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static String makeExceptionMessage(Errors errors) {
		String msg = null;
		if(errors.hasFieldErrors()) {
			List<FieldError> fieldErrors = errors.getFieldErrors();
			msg = fieldErrors.stream().map(FieldError::toString).collect(Collectors.joining(","));
		}
		
		return msg;
	}
	
	/**
	 * CTOR
	 * @param result The validation binding result
	 */
	public ValidationException(Errors errors) {
		this(makeExceptionMessage(errors));
	}

	/**
	 * CTOR
	 * @param message The error message
	 */
	public ValidationException(String string) {
		super(string);
	}
	
}
