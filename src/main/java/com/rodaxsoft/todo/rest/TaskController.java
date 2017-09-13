/*
  TaskController.java

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
 * @author johndev
 *
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
