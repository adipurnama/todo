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

import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.data.TaskRepository;

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
	
	public TaskDAO createTask(TaskDAO taskDao) {
		if(null == taskDao.getTitle()) {
			throw new IllegalArgumentException("title cannot be null");
		}
		
		TaskDAO savedTask = taskRepository.save(taskDao);
		return savedTask;
	}
	
	public List<TaskDAO> getTasks() {
		Sort sort = new Sort(new Order(Direction.ASC, "due").nullsFirst());
		List<TaskDAO> tasks = taskRepository.findAll(sort);
		return tasks;
	}
	
	public void deleteTask(Long id) {
		taskRepository.delete(id);
	}
	
	public TaskDAO updateTask(TaskDAO taskDao) {
		TaskDAO savedTask = taskRepository.findOne(taskDao.getId());
		//Non-modifiable properties: id, created, userId
		taskDao.setId(savedTask.getId());
		taskDao.setCreated(savedTask.getCreated());
		taskDao.setUserId(savedTask.getUserId());
		//Ignore modified, it'll get updated
		
		savedTask = taskRepository.save(taskDao)	;
		return savedTask;
	}
}
