/*
  TaskTest.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.data;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.domain.TaskItem;
import com.rodaxsoft.todo.domain.TaskStatus;

/**
 * TaskTest class
 */
public class TaskTest {
	
	public static TaskItem createMockTask() {
		TaskItem task = new TaskItem();
		task.setDue(LocalDate.now().plusDays(14).toDate());
		task.setCreated(new Date());
		task.setDescription("description of Test task 1 ");
		task.setTitle("Test TaskItem 1");
		task.setId(UUID.randomUUID().toString());
		return task;
	}
	
	@Test
	public void testCreate() {
		
		TaskItem task = createMockTask();

		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
			System.out.println(result);
			task = mapper.readValue(result, TaskItem.class);
			System.out.println(task);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test 
	public void testUpdate() {
		TaskItem task = createMockTask();
		task.setCompleted(new Date());
		task.setModified(new Date());
		task.setStatus(TaskStatus.COMPLETED);

		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
			System.out.println(result);
			task = mapper.readValue(result, TaskItem.class);
			System.out.println(task);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		
		
	}

}
