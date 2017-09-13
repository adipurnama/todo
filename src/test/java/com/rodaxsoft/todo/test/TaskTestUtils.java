/*
  TaskTestUtils.java

  Created by John Boyer on Sep 11, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.test;

import static com.rodaxsoft.todo.security.SecurityConstants.COOKIE_STRING;
import static com.rodaxsoft.todo.security.SecurityConstants.MAX_COOKIE_AGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.todo.data.TaskDAO;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.domain.Profile;
import com.rodaxsoft.todo.security.JSONWebToken;

/**
 * TaskTestUtils class
 *
 */
public class TaskTestUtils {


	public static final String DESCRIPTION = "Description of Test task 1 ";
	public static final Date DUE_DATE = new LocalDate(2017, 10, 31).toDate();
	public static final String TITLE = "Test Task 1";

	public static List<TaskDAO> create100Tasks() {
		List<TaskDAO> tasks = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			TaskDAO dao = createMockTaskDao();
			if(i > 0 && i < 80) {
				Date lastDue = tasks.get(i-1).getDue();
				LocalDate ld = new LocalDate(lastDue.getTime());
				ld = ld.plusDays(1);
				dao.setDue(ld.toDate());
			} else if(i >= 80){
				dao.setDue(null);
			}
			
			tasks.add(dao);
		}
		return tasks;
	}
	
	/**
	 * Creates a cookie with the max age. See {@link #MAX_COOKIE_AGE}.
	 * @param jwt JSON web token
	 * @return A Cookie object
	 */
	public static Cookie createCookie(JSONWebToken jwt) {
		Cookie cookie = new Cookie(COOKIE_STRING, jwt.getAccessToken());
		cookie.setMaxAge(MAX_COOKIE_AGE);
		cookie.setHttpOnly(true);
		cookie.setDomain("localhost");
		cookie.setPath("/");
		return cookie;
	}
	
	public static ApplicationUser createMockApplicationUser() {
		ApplicationUser mockUser = new ApplicationUser();
		mockUser.setEmail("john@example.com");
		mockUser.setName("John Doe");
		mockUser.setPassword("ycPwxjU6");
		return mockUser;
	}
	
	public static String createMockApplicationUserJson() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(createMockApplicationUser());
	}

	public static TaskDAO createMockTaskDao() {
		TaskDAO dao = new TaskDAO();
		dao.setDue(DUE_DATE);
		dao.setDescription(DESCRIPTION);
		dao.setTitle(TITLE);
		return dao;
	}
	
	public static Profile getProfileFromJson(String json) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, String> profileMap = new ObjectMapper().readValue(json, Map.class);
		return new Profile() {
			
			@Override
			public String getAvatarUrl() {
				return null;
			}
			
			@Override
			public String getEmail() {
				return profileMap.get("email");
			}
			
			@Override
			public String getName() {
				return profileMap.get("name");
			}
		};
	}
	
	public static ApplicationUser saveMockApplicationUser(TestBeanProvider provider) {
		ApplicationUser mockUser = createMockApplicationUser();
		//Encrypt password
		mockUser.setPassword(provider.getBCryptPasswordEncoder().encode(mockUser.getPassword()));
		return provider.getApplicationUserRepository().save(mockUser);
	}

}
