/*
  TaskService.java

  The MIT License (MIT)

  Created by John Boyer on Sep 11, 2017
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
package com.rodaxsoft.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.rodaxsoft.task.data.TaskDAO;
import com.rodaxsoft.task.data.TaskRepository;

/**
 * @author johndev
 *
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
