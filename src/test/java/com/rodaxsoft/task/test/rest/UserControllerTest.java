/*
  UserControllerTest.java

  The MIT License (MIT)

  Created by John Boyer on Sep 6, 2017
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

import static com.rodaxsoft.task.test.TaskTestUtils.createCookie;
import static com.rodaxsoft.task.test.TaskTestUtils.getProfileFromJson;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodaxsoft.task.TaskApplication;
import com.rodaxsoft.task.data.ApplicationUserRepository;
import com.rodaxsoft.task.domain.ApplicationUser;
import com.rodaxsoft.task.domain.Profile;
import com.rodaxsoft.task.security.JWTToken;
import com.rodaxsoft.task.test.TaskTestUtils;
import com.rodaxsoft.task.test.TestBeanProvider;


/**
 * UserControllerTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = TaskApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest implements TestBeanProvider {
	private static final String ACCESS_TOKENS_PATH = "/access-tokens";
	
	private static final String REFRESH_ACCESS_TOKENS_PATH = ACCESS_TOKENS_PATH + "/refresh";

	private static final String USERS_PATH = "/users";

	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private MockMvc mvc;
	
	@Override
	public ApplicationUserRepository getApplicationUserRepository() {
		return applicationUserRepository;
	}
	
	@Override
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return bCryptPasswordEncoder;
	}

	/**
	 * @param mockUser
	 * @return
	 */
	private Map<String, String> getCredentialsMap(ApplicationUser mockUser) {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("email", mockUser.getEmail());
		credentials.put("password", mockUser.getPassword());
		return credentials;
	}

	@Test
	public void testGetProfile() {
		
		MvcResult result;
		try {
			//First Create mock user and signup
			String json = TaskTestUtils.createMockApplicationUserJson();
			result = mvc.perform(post(USERS_PATH)
					     .contentType(MediaType.APPLICATION_JSON)
					     .content(json))
					   .andExpect(status().isOk())
					   .andExpect(jsonPath("$.jwt", notNullValue()))
			           .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			MockHttpServletResponse response = result.getResponse();
			JWTToken token = JWTToken.fromJson(response.getContentAsString());
			Assert.assertNotNull(token);
			
			Cookie cookie = createCookie(token);
			
			result = mvc.perform(get("/me")
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(json)
				     .cookie(cookie))
				   .andExpect(status().isOk())
				   .andReturn();
			
			response = result.getResponse();
			Profile profile = getProfileFromJson(response.getContentAsString());
			Assert.assertNotNull(profile);
			Assert.assertNotNull(profile.getEmail());
			Assert.assertNotNull(profile.getName());
			
			//Delete cookie
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			
			//Delete the new user
			ApplicationUser savedUser = applicationUserRepository.findByEmail(TaskTestUtils.createMockApplicationUser().getEmail());
			applicationUserRepository.delete(savedUser);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
	}


	@Test
	public void testLoginUser() {
		//Create a user, bypassing the controller
		ApplicationUser mockUser = TaskTestUtils.createMockApplicationUser();
		ApplicationUser savedUser = TaskTestUtils.saveMockApplicationUser(this);
		
		Map<String, String> credentials = getCredentialsMap(mockUser);
		
		try {
			String json = new ObjectMapper().writeValueAsString(credentials);
			MvcResult result = mvc.perform(post(ACCESS_TOKENS_PATH)
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(json))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("$.jwt", notNullValue()))
			       .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			MockHttpServletResponse response = result.getResponse();			
			JWTToken token = JWTToken.fromJson(response.getContentAsString());
			Assert.assertNotNull(token);
			
			//Delete the new user
			applicationUserRepository.delete(savedUser);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
	}


	@Test
	public void testLogout() {
		
		//Create a user, bypassing the controller signup
		ApplicationUser mockUser = TaskTestUtils.createMockApplicationUser();
		ApplicationUser savedUser = TaskTestUtils.saveMockApplicationUser(this);
		
		Map<String, String> credentials = getCredentialsMap(mockUser);

		try {

			//Perform login first
			String json = new ObjectMapper().writeValueAsString(credentials);
			MvcResult result = mvc.perform(post(ACCESS_TOKENS_PATH)
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(json))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("$.jwt", notNullValue()))
			       .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			MockHttpServletResponse response = result.getResponse();
			
			JWTToken token = JWTToken.fromJson(response.getContentAsString());
			Assert.assertNotNull(token);
			
			Cookie cookie = createCookie(token);
			
			//Then perform logout
			mvc.perform(delete(ACCESS_TOKENS_PATH)
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(token.toRefreshTokenJson())
				     .cookie(cookie))
				   .andExpect(status().isOk());
			
			//Delete the new user
			applicationUserRepository.delete(savedUser);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testRefreshAccessToken() {
		
		//Create a user, bypassing the controller signup
		ApplicationUser mockUser = TaskTestUtils.createMockApplicationUser();
		ApplicationUser savedUser = TaskTestUtils.saveMockApplicationUser(this);

		Map<String, String> credentials = getCredentialsMap(mockUser);
		
		try {

			//Perform login first
			String json = new ObjectMapper().writeValueAsString(credentials);
			MvcResult result = mvc.perform(post(ACCESS_TOKENS_PATH)
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(json))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("$.jwt", notNullValue()))
			       .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			MockHttpServletResponse response = result.getResponse();
			
			JWTToken token = JWTToken.fromJson(response.getContentAsString());
			Assert.assertNotNull(token);
			
			Cookie cookie = createCookie(token);
			
			//Then refresh access token
			result = mvc.perform(post(REFRESH_ACCESS_TOKENS_PATH)
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(token.toRefreshTokenJson())
				     .cookie(cookie))
				   .andExpect(status().isOk())
				   .andExpect(jsonPath("$.jwt", notNullValue())).andReturn();
			
			response = result.getResponse();
			token = JWTToken.fromJson(response.getContentAsString());
			Assert.assertNotNull(token);
			
			//Fetch the profile with new access token
			cookie.setValue(token.getAccessToken());
			result = mvc.perform(get("/me")
				     .contentType(MediaType.APPLICATION_JSON)
				     .content(json)
				     .cookie(cookie))
				   .andExpect(status().isOk())
				   .andReturn();
			
			response = result.getResponse();
			Profile profile = getProfileFromJson(response.getContentAsString());
			Assert.assertNotNull(profile);
			Assert.assertNotNull(profile.getEmail());
			Assert.assertNotNull(profile.getName());
			
			//Delete cookie
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			
			//Delete the new user
			applicationUserRepository.delete(savedUser);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testSignupUser() {	
		try {
			
			String json = TaskTestUtils.createMockApplicationUserJson();
			MvcResult result = mvc.perform(post(USERS_PATH)
					     .contentType(MediaType.APPLICATION_JSON)
					     .content(json))
					   .andExpect(status().isOk())
					   .andExpect(jsonPath("$.jwt", notNullValue()))
			           .andExpect(jsonPath("$.refresh_token", notNullValue())).andReturn();
			
			
			MockHttpServletResponse response = result.getResponse();			
			JWTToken token = JWTToken.fromJson(response.getContentAsString());
			Assert.assertNotNull(token);
			
			//Delete the new user
			String email = TaskTestUtils.createMockApplicationUser().getEmail();
			ApplicationUser savedUser = applicationUserRepository.findByEmail(email);
			applicationUserRepository.delete(savedUser);

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
}
