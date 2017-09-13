/*
  TaskDAOTest.java

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
 *
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
