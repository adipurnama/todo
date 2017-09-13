/*
  TaskController.java

  Created by John Boyer on Sep 12, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.rest;

import static com.rodaxsoft.todo.security.SecurityConstants.COOKIE_STRING;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.service.ApplicationUserService;
import com.rodaxsoft.todo.service.TaskService;
import com.rodaxsoft.todo.validation.TaskCreateAndUpdateValidator;
import com.rodaxsoft.todo.validation.ValidationException;

/**
 * TaskController class
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	private TaskService taskService;
	private ApplicationUserService userService;
	@Autowired
	private TaskCreateAndUpdateValidator taskValidator;

	@Autowired
	public TaskController(TaskService taskService, ApplicationUserService userService) {
		this.taskService = taskService;
		this.userService = userService;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public TaskDAO createTask(@RequestBody TaskDAO task, BindingResult result, @CookieValue(COOKIE_STRING) String token) {
		taskValidator.validate(task, result);
		if(result.hasErrors()) {
			throw new ValidationException(result);
		}
		
		//Set user id here!
		Long userId = userService.getUserIdForToken(token);
		task.setUserId(userId);
		
		return taskService.createTask(task);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<TaskDAO> getTasks(@CookieValue(COOKIE_STRING) String token) {
		return taskService.getTasks();	
	}
	
	@PutMapping(path = "/{id}")
	public TaskDAO updateTask(@PathVariable Long id, @RequestBody TaskDAO updatedTask, BindingResult result) {
		taskValidator.validate(updatedTask, result);
		if (result.hasFieldErrors()) {
			throw new ValidationException(result);
		}
		
		updatedTask.setId(id);
		return taskService.updateTask(updatedTask);
	}
	
	 @DeleteMapping(path = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
		public String deleteTask(@PathVariable Long id) {
			taskService.deleteTask(id);
			return "";
		}

}
