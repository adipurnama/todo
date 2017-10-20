/*
  TaskServiceTest.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.DynamoDBUserMapper;
import com.rodaxsoft.todo.domain.TaskItem;
import com.rodaxsoft.todo.domain.TaskStatus;
import com.rodaxsoft.todo.domain.UserItem;
import com.rodaxsoft.todo.service.TaskService;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * TaskServiceTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {
	@Autowired
	private TaskService taskService;
	
	private String userId;
	
	@Autowired
	private DynamoDBUserMapper userRepository; 
	
	/**
	 * @return
	 */
	private TaskItem createMockTask() {
		TaskItem task = new TaskItem();
		task.setDue(LocalDate.now().plusDays(14).toDate());
		task.setDescription("Description of Test task 1 ");
		task.setTitle("Test TaskItem 1");
		task.setUserId(userId);
		return task;
	}
	
	@Before
	public void setup() {
		UserItem user = userRepository.createUser(TaskTestUtils.createMockApplicationUser());
		userId = user.getId();
	}
	
	@Test
	public void testCreateTask() {
		TaskItem savedTask = taskService.createTask(createMockTask());
		System.out.println(savedTask);
		Assert.assertNotNull(savedTask.getCreated());
		Assert.assertNotNull(savedTask.getId());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedTask);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteTask() {
		TaskItem savedTask = taskService.createTask(createMockTask());
		taskService.deleteTask(savedTask.getId());
		System.out.println(savedTask);
		assertTrue(!taskService.exists(savedTask));
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedTask);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetTasks() {
		List<TaskItem> tasks = TaskTestUtils.create100Tasks(userId);
		TaskItem prev = null;
		for (TaskItem task : tasks) {
			TaskItem savedTask = taskService.createTask(task);
			System.out.println(savedTask);
			Assert.assertNotNull(savedTask.getCreated());
			Assert.assertNotNull(savedTask.getId());
			
			if(prev != null && prev.getDue() != null && task.getDue() != null) {
				Assert.assertTrue(prev.getDue().before(task.getDue()));
			}
			
			prev = task;
		}
		
		tasks = taskService.getTasks(userId);
		Assert.assertNotNull(tasks);
		Assert.assertTrue(tasks.size() >= 100);
		System.out.println(tasks);
	}
	
	@Test
	public void testUpdateTask() {
		TaskItem savedTask = taskService.createTask(createMockTask());
		savedTask.setStatus(TaskStatus.COMPLETED);
		savedTask.setCompleted(new Date());
		savedTask = taskService.updateTask(savedTask);
		System.out.println(savedTask);
		Assert.assertNotNull(savedTask.getModified());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedTask);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}

}
