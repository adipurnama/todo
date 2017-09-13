/*
  JWTToken.java

  The MIT License (MIT)

  Created by John Boyer on Sep 5, 2017
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
package com.rodaxsoft.task.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JWTToken class
 *
 */
public class JWTToken implements JSONWebToken {
	
	public static final String ACCESS_TOKEN_JSON_KEY = "jwt";
	public static final String REFRESH_TOKEN_JSON_KEY = "refresh_token";
	
	public static JWTToken fromJson(String json) throws IOException {
		return new ObjectMapper().readValue(json, JWTToken.class);
	}
	
	public static String toAccessTokenJson(String accessToken) throws JsonProcessingException {
		Map<String, String> mapToken = new HashMap<>();
		mapToken.put(ACCESS_TOKEN_JSON_KEY, accessToken);
		return new ObjectMapper().writeValueAsString(mapToken);
	}
	
	public static String toRefreshTokenJson(String refreshToken) throws JsonProcessingException {
		Map<String, String> mapToken = new HashMap<>();
		mapToken.put(REFRESH_TOKEN_JSON_KEY, refreshToken);
		return new ObjectMapper().writeValueAsString(mapToken);
	}
	
	private String accessToken;

	private String refreshToken;
	
	public JWTToken() {
		
	}
	
	JWTToken(String token, String refreshToken) {
		this.accessToken = token;
		this.refreshToken = refreshToken;
	}
	/**
	 * @return the access token
	 */
	@Override
	@JsonGetter(ACCESS_TOKEN_JSON_KEY)
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * @return the refresh token
	 */
	@Override
	@JsonGetter(REFRESH_TOKEN_JSON_KEY)
	public String getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * @param token the token to set
	 */
	@JsonSetter(ACCESS_TOKEN_JSON_KEY)
	public void setAccessToken(String token) {
		this.accessToken = token;
	}
	
	/**
	 * @param refreshToken the refreshToken to set
	 */
	@JsonSetter(REFRESH_TOKEN_JSON_KEY)
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String toAccessTokenJson() throws JsonProcessingException {
		return toAccessTokenJson(accessToken);
	}
	
	public String toRefreshTokenJson() throws JsonProcessingException {
		return toRefreshTokenJson(refreshToken);
	}
	
}
