/*
  JWTUtilTest.java

  The MIT License (MIT)

  Created by John Boyer on Sep 7, 2017
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
package com.rodaxsoft.task.test;

import static com.rodaxsoft.task.security.JWTUtil.generateJsonWebToken;
import static com.rodaxsoft.task.security.JWTUtil.parseToken;
import static com.rodaxsoft.task.security.JWTUtil.parseTokenClaims;
import static com.rodaxsoft.task.security.JWTUtil.refreshAccessToken;
import static com.rodaxsoft.task.test.TaskTestUtils.createMockApplicationUser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.rodaxsoft.task.domain.ApplicationUser;
import com.rodaxsoft.task.security.JSONWebToken;

import io.jsonwebtoken.Claims;

/**
 * JWTUtilTest class
 */
@RunWith(SpringRunner.class)
public class JWTUtilTest  {
	
	
	private JSONWebToken createToken() {
		return generateJsonWebToken(createMockApplicationUser());
	}
	
	@Test
	public void testGenerateJsonWebToken() {
		JSONWebToken webToken = createToken();
		final String accessToken = webToken.getAccessToken();
		String username = parseToken(accessToken).getUsername();
		
		final ApplicationUser mockUser = createMockApplicationUser();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		final String refreshToken = webToken.getRefreshToken();
		username = parseToken(refreshToken).getUsername();
		
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		Claims accessTokenClaims = parseTokenClaims(accessToken);
		Claims refreshTokenClaims = parseTokenClaims(refreshToken);
		
		Assert.assertTrue(accessTokenClaims.getExpiration().before(refreshTokenClaims.getExpiration()));
	}
	
	@Test
	public void testParseToken() {
		JSONWebToken webToken = createToken();
		final String accessToken = webToken.getAccessToken();
		String username = parseToken(accessToken).getUsername();
		
		final ApplicationUser mockUser = createMockApplicationUser();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		final String refreshToken = webToken.getRefreshToken();
		username = parseToken(refreshToken).getUsername();
		
		Assert.assertTrue(username.equals(mockUser.getUsername()));
	}
	
	@Test
	public void testRefreshAccessToken() {
		JSONWebToken webToken = createToken();
		String accessToken = webToken.getAccessToken();
		String username = parseToken(accessToken).getUsername();
		
		final ApplicationUser mockUser = createMockApplicationUser();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		final String refreshToken = webToken.getRefreshToken();
		username = parseToken(refreshToken).getUsername();
		
		Assert.assertTrue(username.equals(mockUser.getUsername()));
		
		Claims accessTokenClaims = parseTokenClaims(accessToken);
		Claims refreshTokenClaims = parseTokenClaims(refreshToken);
		
		Assert.assertTrue(accessTokenClaims.getExpiration().before(refreshTokenClaims.getExpiration()));
		
		accessToken = refreshAccessToken(webToken);
		Assert.assertNotNull(accessToken);	
		
		username = parseToken(accessToken).getUsername();
		Assert.assertTrue(username.equals(mockUser.getUsername()));
	}
	
	
	

}
