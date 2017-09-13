/*
  ApplicationUserService.java

  The MIT License (MIT)

  Created by John Boyer on Sep 9, 2017
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
package com.rodaxsoft.todo.service;

import static com.rodaxsoft.todo.security.JWTUtil.generateJsonWebToken;
import static com.rodaxsoft.todo.security.JWTUtil.parseToken;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodaxsoft.todo.data.ApplicationUserRepository;
import com.rodaxsoft.todo.domain.ApplicationUser;
import com.rodaxsoft.todo.security.JSONWebToken;
import com.rodaxsoft.todo.security.JWTToken;
import com.rodaxsoft.todo.security.JWTUtil;
import com.rodaxsoft.todo.validation.StoredApplicationUserProvider;

import io.jsonwebtoken.JwtException;

/**
 * ApplicationUserService class
 */
@Service
public class ApplicationUserService implements StoredApplicationUserProvider {
	
	private static final Log LOG = LogFactory.getLog(ApplicationUserService.class);
	
	/**
	 * ApplicationUserRepository
	 */
	private ApplicationUserRepository applicationUserRepository;
	/**
	 * BCryptPasswordEncoder
	 */
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public ApplicationUserService(ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.applicationUserRepository = applicationUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return bCryptPasswordEncoder;
	}
	
	/**
	 * @param user
	 * @return
	 */
	public JSONWebToken loginUser(ApplicationUser user) {
		final String email = user.getEmail();
		ApplicationUser savedUser = storedApplicationUserForEmail(email);
		JSONWebToken jwt = generateJsonWebToken(savedUser);
		
		LOG.info("User logged-in");
		return jwt;
	}
	
	/**
	 * @param user
	 * @param result
	 * @return
	 */
	public JSONWebToken signUpUser(ApplicationUser user) {
		ApplicationUser savedUser = applicationUserRepository.findByEmail(user.getEmail());
		if(savedUser != null) {
			throw new IllegalArgumentException("User already exists");
		}
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		applicationUserRepository.save(user);
		JSONWebToken jwt = generateJsonWebToken(user);
		
		LOG.info("User created");

		return jwt;
	}
	
	public void logoutUser(JSONWebToken token, ApplicationUserServiceListener listener) {
		String refreshToken = token.getRefreshToken();
		String accessToken = token.getAccessToken();
		if(null == refreshToken) {
			throw new IllegalArgumentException("refresh token cannot be null");
		} else if(JWTUtil.isTokenExpired(refreshToken)) {
			throw new JwtException("Refresh token expired");
		}
		
		if(JWTUtil.isTokenExpired(accessToken)) {
			LOG.info("Access token already expired");
		} else if(listener != null){
			listener.shouldExpireAccessToken();
		}
	}
	
	public  Map<String, String> refreshAccessToken(JSONWebToken token, ApplicationUserServiceListener listener) {
		String refreshToken = token.getRefreshToken();
		if(null == refreshToken) {
			throw new IllegalArgumentException("refresh token cannot be null");
		} else if(JWTUtil.isTokenExpired(refreshToken)) {
			throw new JwtException("Refresh token expired");
		}
		
		String newAccessToken = JWTUtil.refreshAccessToken(token);
		
		if(listener != null) {
			listener.shouldExpireAccessToken();
		}
		
		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put(JWTToken.ACCESS_TOKEN_JSON_KEY, newAccessToken);
		
		return tokenMap;
	}
	
	
	public Long getUserIdForToken(String token) {
		String username = parseToken(token).getUsername();
		ApplicationUser user = storedApplicationUserForEmail(username);
		if(null == user) {
			throw new IllegalArgumentException("User not found");
		}

		return user.getId();
	}


	@Override
	public ApplicationUser storedApplicationUserForEmail(String email) {
		return applicationUserRepository.findByEmail(email);
	}

}
