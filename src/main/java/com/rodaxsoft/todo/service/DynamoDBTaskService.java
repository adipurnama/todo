/*
  DynamoDBTaskService.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodaxsoft.todo.data.DynamoDBTaskMapper;
import com.rodaxsoft.todo.domain.TaskItem;
import com.rodaxsoft.todo.exception.ResourceNotFoundException;
import com.rodaxsoft.todo.exception.ValidationException;

/**
 * DynamoDBTaskService class
 */
@Service
public class DynamoDBTaskService implements TaskService {
	
	@Autowired
	private DynamoDBTaskMapper taskMapper;

	@Override
	public TaskItem createTask(TaskItem task) {
		if(null == task.getTitle()) {
			throw new ValidationException("title cannot be null");
		}
		
		TaskItem savedTask = taskMapper.createTask(task);
		return savedTask;
	}
	
	@Override
	public void deleteTask(String id) {
		if(!taskMapper.exists(id)) {
			throw new ResourceNotFoundException("TaskItem not found");
		}
		
		taskMapper.deleteTaskById(id);
	}
	
	@Override
	public boolean exists(String id) {
		return taskMapper.exists(id);
	}
	
	@Override
	public boolean exists(TaskItem task) {
		return exists(task.getId());
	}
	
	@Override
	public List<TaskItem> getTasks(String userId) {
		List<TaskItem> tasks = taskMapper.findByUserId(userId);
		return tasks;
	}
	
	@Override
	public boolean hasTasks(String userId) {
		return taskMapper.hasTasks(userId);
	}

	@Override
	public TaskItem updateTask(TaskItem task) {
		final String id = task.getId();
		if(!taskMapper.exists(id)) {
			throw new ResourceNotFoundException("TaskItem not found");
		}
		
		TaskItem savedTask = taskMapper.findById(id);
		//Non-modifiable properties: id, created, userId
		task.setId(savedTask.getId());
		task.setCreated(savedTask.getCreated());
		task.setUserId(savedTask.getUserId());
		//Ignore modified, it'll get updated
		
		savedTask = taskMapper.updateTask(task)	;
		return savedTask;
	}
}
