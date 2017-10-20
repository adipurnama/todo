/*
  TaskMapperTest.java

  Created by John Boyer on Oct 16, 2017
  Copyright (c) 2017 Rodax Software, Inc.
  
  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rodaxsoft.todo.data.DynamoDBTaskMapper;
import com.rodaxsoft.todo.domain.TaskItem;
import com.rodaxsoft.todo.domain.UserItem;
import com.rodaxsoft.todo.exception.DuplicateItemException;

/**
 * TaskMapperTest class
 * 
 * @author John Boyer
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskMapperTest {

	@Autowired
	private DynamoDBTaskMapper taskMapper;

	private final static Log log = LogFactory.getLog(TaskMapperTest.class);
	/*
	 * AmazonDynamoDB client

	private static AmazonDynamoDB sClient;
	/*
	 * DynamoDBProxyServer instance

	private static DynamoDBProxyServer sServer;


	private static void createAmazonDynamoDBClient() {
		sClient = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", " us-west-2")).build();
	}

	private static void createTaskTables() {
		// Create task tables
		DynamoDBMapper mapper = new DynamoDBMapper(sClient);
		CreateTableRequest tableRequest = mapper.generateCreateTableRequest(TaskItem.class);
		tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		CreateTableResult result = sClient.createTable(tableRequest);

		tableRequest = mapper.generateCreateTableRequest(UserItem.class);
		tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		result = sClient.createTable(tableRequest);

		tableRequest = mapper.generateCreateTableRequest(TaskLookupItem.class);
		tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		result = sClient.createTable(tableRequest);
	}

	/**
	 * Starts an in-memory local DynamoDB server
	 * 
	 * @see https://stackoverflow.com/a/31845157/314897

	@BeforeClass
	public static void runDynamoDB() {

		// //Need to set the SQLite4Java library path to avoid a linker error
		System.setProperty("sqlite4java.library.path", "./build/libs/");

		// Create an in-memory and in-process instance of DynamoDB Local that runs over HTTP
		final String[] localArgs = { "-inMemory" };

		try {
			sServer = ServerRunner.createServerFromCommandLineArgs(localArgs);
			sServer.start();

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			return;
		}

		createAmazonDynamoDBClient();

		createTaskTables();
	}

	/**
	 * Stops the local DynamoDB server
	 * 
	 * @see https://stackoverflow.com/a/31845157/314897

	@AfterClass
	public static void shutdownDynamoDB() {
		if(sServer != null) {
			try {
				sServer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
*/
	/**
	 * User object
	 */
	private UserItem user;

	private TaskItem createMockTaskItem() {
		LocalDate ld = LocalDate.now().plusDays(14);
		Date due = java.sql.Date.valueOf(ld);
		TaskItem task = new TaskItem();
		task.setDue(due);
		task.setDescription("My interesting task description");
		task.setTitle("Test task 1");
		task.setUserId(user.getEmail());
		return task;
	}

	@Before
	public void setUp() {
		user = new UserItem();
		user.setEmail("john@example.com");
		user.setName("John Doe");
		user.setPassword("The1Passor6d");
	}

	@After
	public void tearDown() {
		if(taskMapper.hasTasks(user.getEmail())) {
			List<TaskItem> taskItems = taskMapper.findByUserId(user.getEmail());
			for (TaskItem taskItem : taskItems) {
				taskMapper.deleteTaskById(taskItem.getId());
			}
		}
	}

	@Test
	public void testCreateTask() {

		TaskItem task = createMockTaskItem();

		// Create the new tasks in Task table
		TaskItem savedTask;
		try {
			savedTask = taskMapper.createTask(task);
			Assert.assertNotNull(savedTask.getId());
		} catch (DuplicateItemException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testDeleteTask() {

		TaskItem task = createMockTaskItem();
		TaskItem savedTask;
		try {
			// Create the task first
			savedTask = taskMapper.createTask(task);
			taskMapper.deleteTaskById(savedTask.getId());
			Assert.assertTrue(null == taskMapper.find(savedTask));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testfindByUserId() {

		for (int i = 0; i < 10; i++) {
			taskMapper.createTask(createMockTaskItem());
		}

		// TEST GET
		List<TaskItem> taskItems;
		try {
			taskItems = taskMapper.findByUserId(user.getEmail());
			Assert.assertNotNull(taskItems);

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testUpdateTask() {

		// Create a new task and save it in the db
		TaskItem task = createMockTaskItem();
		TaskItem savedTask;
		try {
			savedTask = taskMapper.createTask(task);
			Assert.assertNotNull(savedTask.getId());
		} catch (DuplicateItemException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		// Update the task
		task.setDescription("My extremely interesting task description");
		java.sql.Date due = new java.sql.Date(task.getDue().getTime());

		LocalDate ld = due.toLocalDate();
		ld.plusDays(3);
		task.setDue(java.sql.Date.valueOf(ld));
		try {
			taskMapper.updateTask(task);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		// Query for the task
		savedTask = taskMapper.find(task);

		Assert.assertNotNull(savedTask.getId());
		Assert.assertEquals(java.sql.Date.valueOf(ld), savedTask.getDue());
	}

}
