/*
  TaskDAOTest.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test.data;

import java.io.IOException;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.data.TaskStatus;

/**
 * TaskDAOTest class
 */
public class TaskDAOTest {
	
	public static TaskDAO createMockTaskDAO() {
		TaskDAO dao = new TaskDAO();
		dao.setDue(new LocalDate(2017, 10, 31).toDate());
		dao.setCreated(new Date());
		dao.setDescription("description of Test task 1 ");
		dao.setTitle("Test Task 1");
		dao.setId(1L);
		return dao;
	}
	
	@Test
	public void testCreate() {
		
		TaskDAO dao = createMockTaskDAO();

		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dao);
			System.out.println(result);
			dao = mapper.readValue(result, TaskDAO.class);
			System.out.println(dao);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test 
	public void testUpdate() {
		TaskDAO dao = createMockTaskDAO();
		dao.setCompleted(new Date());
		dao.setModified(new Date());
		dao.setStatus(TaskStatus.COMPLETED);

		try {
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dao);
			System.out.println(result);
			dao = mapper.readValue(result, TaskDAO.class);
			System.out.println(dao);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		
		
	}

}
