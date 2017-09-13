/*
  ApplicationUserServiceTest.java

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
package com.rodaxsoft.todo.test.service;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rodaxsoft.todo.data.ApplicationUserRepository;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.security.JSONWebToken;
import com.rodaxsoft.todo.service.ApplicationUserService;
import com.rodaxsoft.todo.test.TaskTestUtils;

/**
 * ApplicationUserServiceTest class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationUserServiceTest {
	
	@Autowired
	private ApplicationUserRepository userRepository;
	
	@Autowired
	private ApplicationUserService userService;
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
	}

	private JSONWebToken createUser() {
		ApplicationUser user = TaskTestUtils.createMockApplicationUser();
		JSONWebToken token = userService.signUpUser(user);
		Assert.assertNotNull(token);
		Assert.assertNotNull(token.getAccessToken());
		Assert.assertNotNull(token.getRefreshToken());
		return token;
	}
	
	@Test
	public void testLogin() {
		//Create user in the database
		JSONWebToken token = createUser();
		
		ApplicationUser user = TaskTestUtils.createMockApplicationUser();
		token = userService.loginUser(user);
		Assert.assertNotNull(token);
		Assert.assertNotNull(token.getAccessToken());
		Assert.assertNotNull(token.getRefreshToken());
	}
	
	@Test
	public void testLogout() {
		// Create user in the database
		JSONWebToken token = createUser();

		try {
			userService.logoutUser(token, () -> {
				System.out.println("Expire token here!");
			});
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
	}
	
	
	@Test
	public void testRefreshAccessToken() {
		// Create user in the database
		JSONWebToken token = createUser();
		
		try {
			Map<String, String> tokenMap = userService.refreshAccessToken(token, () -> {
				System.out.println("Expire token here!");
			});
			
			Assert.assertNotNull(tokenMap);
			Assert.assertNotNull(tokenMap.get("jwt"));
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testSignup() {
		JSONWebToken token = createUser();
		System.out.println(token);
	}

}
