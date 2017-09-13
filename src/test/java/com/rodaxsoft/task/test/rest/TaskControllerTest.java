/*
  TaskControllerTest.java

  The MIT License (MIT)

  Created by John Boyer on Sep 12, 2017
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
package com.rodaxsoft.task.test.rest;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.List;

import javax.servlet.http.Cookie;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rodaxsoft.task.TaskApplication;
import com.rodaxsoft.task.data.ApplicationUserRepository;
import com.rodaxsoft.task.data.TaskDAO;
import com.rodaxsoft.task.data.TaskRepository;
import com.rodaxsoft.task.domain.ApplicationUser;
import com.rodaxsoft.task.security.JWTToken;
import com.rodaxsoft.task.service.ApplicationUserService;
import com.rodaxsoft.task.service.TaskService;
import com.rodaxsoft.task.test.TaskTestUtils;
import com.rodaxsoft.task.test.UploadableTestTask;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = TaskApplication.class)
@AutoConfigureMockMvc
public class TaskControllerTest {
	

	private static final String TASKS_ENDPOINT = "/tasks";
	
	private Cookie cookie;
	
	@Autowired
	private MockMvc mvc;
	
	private MockHttpServletResponse response;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ApplicationUserRepository userRepository;
	
	@Autowired
	private ApplicationUserService userService;
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
		taskRepository.deleteAll();
	}
	
	@Before
	public void setUp() {
		
		try {
			ApplicationUser user = TaskTestUtils.createMockApplicationUser();
			String json = new ObjectMapper().writeValueAsString(user);
			MvcResult result;
			
			result = mvc.perform(post("/users")
					     .contentType(MediaType.APPLICATION_JSON)
					     .content(json))
					   .andExpect(status().isOk())
					   .andExpect(jsonPath("$.jwt", notNullValue()))
			           .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			MockHttpServletResponse localRes = result.getResponse();
			JWTToken token = JWTToken.fromJson(localRes.getContentAsString());
			
			cookie = TaskTestUtils.createCookie(token);
		
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testCreateTask() {
		try {
			
			TaskDAO taskDao = TaskTestUtils.createMockTaskDao();
			String json = new ObjectMapper().writeValueAsString(taskDao);
			MvcResult result = mvc.perform(post(TASKS_ENDPOINT)
					                .contentType(MediaType.APPLICATION_JSON)
					                .content(json).cookie(cookie))
					                            .andExpect(status().isOk())
					                            .andExpect(jsonPath("$.id", notNullValue()))
					                            .andExpect(jsonPath("$.due", notNullValue()))
					                            .andExpect(jsonPath("$.title", notNullValue()))
					                            .andExpect(jsonPath("$.status", notNullValue()))
					                            .andExpect(jsonPath("$.created", notNullValue()))
					                            .andDo(print())
					               .andReturn();
			
			response = result.getResponse();
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}
	
	@Test
	public void testDeleteTask() {
		TaskDAO dao = TaskTestUtils.createMockTaskDao();
		dao = taskService.createTask(dao);

		
		try {
			MvcResult result;
			result = mvc.perform(delete(TASKS_ENDPOINT + "/" + dao.getId())
			        .contentType(MediaType.TEXT_PLAIN_VALUE)
			               .cookie(cookie))
			                    .andExpect(status().isOk())
			                    .andDo(print())
			       .andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			Assert.fail(e.getMessage());

		}
	}
	
	@Test
	public void testGetTasks() {
		List<TaskDAO> tasks = TaskTestUtils.create100Tasks();
		Long userId = userService.getUserIdForToken(cookie.getValue());
		for (TaskDAO taskDao : tasks) {
			taskDao.setUserId(userId);
			taskService.createTask(taskDao);
		}
		
		MvcResult result;
		try {

			result = mvc.perform(get(TASKS_ENDPOINT)
			        .contentType(MediaType.APPLICATION_JSON)
			        .cookie(cookie))
			                    .andExpect(status().isOk())
	                            .andExpect(jsonPath("$[*].id", notNullValue()))
	                            .andExpect(jsonPath("$[*].due", notNullValue()))
	                            .andExpect(jsonPath("$[*].title", notNullValue()))
	                            .andExpect(jsonPath("$[*].status", notNullValue()))
	                            .andExpect(jsonPath("$[*].created", notNullValue()))
			                    .andDo(print())
			       .andReturn();
			
			response = result.getResponse();
			
			ObjectMapper mapper = new ObjectMapper();
			TypeFactory factory = mapper.getTypeFactory();
			CollectionType type = factory.constructCollectionType(List.class, TaskDAO.class);
			String json = response.getContentAsString();
			tasks = mapper.readValue(json, type);
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("test-tasks.json"), tasks);
			 
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}		
	}
	
	@Test
	public void testUpdateTask() {
		TaskDAO dao = TaskTestUtils.createMockTaskDao();
		dao = taskService.createTask(dao);
		dao.setDescription("All new description");
		dao.setTitle("My brand new title");
		
		UploadableTestTask upldTask = new UploadableTestTask();
		BeanUtils.copyProperties(dao, upldTask);

		try {
			
			String json = new ObjectMapper().writeValueAsString(upldTask);
			MvcResult result = mvc.perform(put(TASKS_ENDPOINT + "/" + dao.getId())
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(json).cookie(cookie))
			                    .andExpect(status().isOk())
			                    .andExpect(jsonPath("$.id", notNullValue()))
	                            .andExpect(jsonPath("$.due", notNullValue()))
	                            .andExpect(jsonPath("$.title", notNullValue()))
	                            .andExpect(jsonPath("$.status", notNullValue()))
	                            .andExpect(jsonPath("$.created", notNullValue()))
	                            .andExpect(jsonPath("$.description", notNullValue()))
	                            .andExpect(jsonPath("$.modified", notNullValue()))
			                    .andDo(print())
			       .andReturn();
			
			response = result.getResponse();
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}		
	}

}
