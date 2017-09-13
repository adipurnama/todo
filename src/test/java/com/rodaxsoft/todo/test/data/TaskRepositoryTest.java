/*
  TaskRepositoryTest.java

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

import static com.rodaxsoft.todo.test.TaskTestUtils.DESCRIPTION;
import static com.rodaxsoft.todo.test.TaskTestUtils.DUE_DATE;
import static com.rodaxsoft.todo.test.TaskTestUtils.TITLE;
import static com.rodaxsoft.todo.test.TaskTestUtils.createMockTaskDao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.data.TaskRepository;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * TaskRepositoryTest class
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@After
	public void cleanup() {
		taskRepository.deleteAll();
	}
	
	
	@Test
	public void testGetTasks() {
		//Save a task
		TaskDAO dao = createMockTaskDao();
		dao = taskRepository.save(dao);
		
		List<TaskDAO> tasks = taskRepository.findAll();
		Assert.assertSame(1, tasks.size());
		Assert.assertSame(1L, tasks.get(0).getId());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tasks);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
	}

	
	@Test
	public void testSaveTask() {
		
		TaskDAO dao = TaskTestUtils.createMockTaskDao();
		
		dao = taskRepository.save(dao);
		System.out.println();
		System.out.println(dao);
		System.out.println();
		
		Assert.assertSame(DESCRIPTION, dao.getDescription());
		Assert.assertSame(TITLE, dao.getTitle());
		Assert.assertSame(DUE_DATE, dao.getDue());
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dao);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testUpdateTask() {

		// Save a task
		TaskDAO dao = createMockTaskDao();
		dao = taskRepository.save(dao);
		
		final String desc = "An all new description";
		dao.setDescription(desc);
		final String title = "The title has changed";
		dao.setTitle(title);
		
		//Update the task
		dao = taskRepository.save(dao);
		Assert.assertSame(desc, dao.getDescription());
		Assert.assertSame(title, dao.getTitle());
		Assert.assertSame(TaskTestUtils.DUE_DATE, dao.getDue());
		
		
		try {
			String result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dao);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			Assert.fail(e.getMessage());
		}

	}

}
