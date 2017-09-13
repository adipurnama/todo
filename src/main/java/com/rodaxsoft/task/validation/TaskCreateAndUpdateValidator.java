/*
  TaskCreateAndUpdateValidator.java

  The MIT License (MIT)

  Created by John Boyer on Sep 12, 2017
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
package com.rodaxsoft.task.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rodaxsoft.task.data.TaskDAO;
import com.rodaxsoft.task.data.TaskStatus;

/**
 * TaskCreateAndUpdateValidator class
 *
 */
public class TaskCreateAndUpdateValidator implements Validator {

	/**
	 * 
	 */
	private static final String FIELD_EMPTY_MSG = "Field is empty";
	/**
	 * 
	 */
	private static final String FIELD_EMPTY_CODE = "field.empty";
	/**
	 * 
	 */
	private static final String FIELD_READONLY_MSG = "Field is readonly";
	/**
	 * 
	 */
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
