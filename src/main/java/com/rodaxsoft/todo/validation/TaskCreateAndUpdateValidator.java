/*
  TaskCreateAndUpdateValidator.java

  Created by John Boyer on Sep 12, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.data.TaskStatus;

/**
 * TaskCreateAndUpdateValidator class
 *
 */
public class TaskCreateAndUpdateValidator implements Validator {

	private static final String FIELD_EMPTY_MSG = "Field is empty";

	private static final String FIELD_EMPTY_CODE = "field.empty";
	
	private static final String FIELD_READONLY_MSG = "Field is readonly";
	
	private static final String FIELD_READONLY_CODE = "field.readonly";


	@Override
	public boolean supports(Class<?> clazz) {
		return TaskDAO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "field.required", "Title required");
		TaskDAO dao = (TaskDAO) target;
		
		if(dao.getCreated() != null) {
			errors.rejectValue("created", FIELD_READONLY_CODE, FIELD_READONLY_MSG);			
		}
		
		if(dao.getModified() != null) {
			errors.rejectValue("modified", FIELD_READONLY_CODE, FIELD_READONLY_MSG);
		}
		
		if(dao.getUserId() != null) {
			errors.rejectValue("userId", FIELD_READONLY_CODE, FIELD_READONLY_MSG);
		}
		
		//Check for empty description if non-null
		String description = dao.getDescription();
		if(description != null && description.isEmpty()) {
			errors.rejectValue("description", FIELD_EMPTY_CODE, FIELD_EMPTY_MSG);			
		}
		
		if(dao.getId() != null) {
			errors.rejectValue("userId", FIELD_READONLY_CODE, FIELD_READONLY_MSG);
		}
		
		TaskStatus status = dao.getStatus();
		if((dao.getCompleted() != null && status != TaskStatus.COMPLETED) || 
		   (status != null && status == TaskStatus.RESERVED) ) {
			errors.rejectValue("status", "field.invalid", "Invalid status");			
		}
	}

}
