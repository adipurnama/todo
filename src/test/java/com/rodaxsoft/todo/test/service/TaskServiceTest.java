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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.ApplicationUserRepository;
import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.data.TaskStatus;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.service.TaskService;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * TaskServiceTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {
	@Autowired
	private TaskRepository taskRepsitory;
	
	@Autowired
	private TaskService taskService;
	
	private Long userId;
	
	@Autowired
	private ApplicationUserRepository userRepository; 
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
		taskRepsitory.deleteAll();
	}
	
	/**
	 * @return
	 */
	private TaskDAO createMockTaskDAO() {
		TaskDAO dao = new TaskDAO();
		dao.setDue(new LocalDate(2017, 10, 31).toDate());
		dao.setDescription("Description of Test task 1 ");
		dao.setTitle("Test Task 1");
		dao.setUserId(userId);
		return dao;
	}
	
	@Before
	public void setup() {
		ApplicationUser user = userRepository.save(TaskTestUtils.createMockApplicationUser());
		userId = user.getId();
	}
	
	
	@Test
	public void testCreateTask() {
		TaskDAO savedDao = taskService.createTask(createMockTaskDAO());
		System.out.println(savedDao);
		Assert.assertNotNull(savedDao.getCreated());
		Assert.assertNotNull(savedDao.getId());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedDao);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteTask() {
		TaskDAO savedDao = taskService.createTask(createMockTaskDAO());
		taskService.deleteTask(savedDao.getId());
		System.out.println(savedDao);
		assertTrue(taskService.getTasks().isEmpty());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedDao);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetTasks() {
		List<TaskDAO> tasks = TaskTestUtils.create100Tasks();
		TaskDAO prev = null;
		for (TaskDAO taskDao : tasks) {
			TaskDAO savedDao = taskService.createTask(taskDao);
			System.out.println(savedDao);
			Assert.assertNotNull(savedDao.getCreated());
			Assert.assertNotNull(savedDao.getId());
			
			if(prev != null && prev.getDue() != null && taskDao.getDue() != null) {
				Assert.assertTrue(prev.getDue().before(taskDao.getDue()));
			}
			
			prev = taskDao;
		}
		
		tasks = taskService.getTasks();
		System.out.println(tasks);
	}
	
	@Test
	public void testUpdateTask() {
		TaskDAO savedDao = taskService.createTask(createMockTaskDAO());
		savedDao.setStatus(TaskStatus.COMPLETED);
		savedDao.setCompleted(new Date());
		savedDao = taskService.updateTask(savedDao);
		System.out.println(savedDao);
		Assert.assertNotNull(savedDao.getModified());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(savedDao);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}

}
