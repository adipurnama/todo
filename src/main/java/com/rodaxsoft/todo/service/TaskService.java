/*
  TaskService.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.domain.Task;

/**
 * TaskService class
 */
@Service
public class TaskService {
	
	
	private TaskRepository taskRepository;

	@Autowired
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	public Task createTask(Task task) {
		if(null == task.getTitle()) {
			throw new IllegalArgumentException("title cannot be null");
		}
		
		Task savedTask = taskRepository.save(task);
		return savedTask;
	}
	
	public List<Task> getTasks() {
		Sort sort = new Sort(new Order(Direction.ASC, "due").nullsFirst());
		List<Task> tasks = taskRepository.findAll(sort);
		return tasks;
	}
	
	public void deleteTask(Long id) {
		taskRepository.delete(id);
	}
	
	public Task updateTask(Task task) {
		Task savedTask = taskRepository.findOne(task.getId());
		//Non-modifiable properties: id, created, userId
		task.setId(savedTask.getId());
		task.setCreated(savedTask.getCreated());
		task.setUserId(savedTask.getUserId());
		//Ignore modified, it'll get updated
		
		savedTask = taskRepository.save(task)	;
		return savedTask;
	}
}
