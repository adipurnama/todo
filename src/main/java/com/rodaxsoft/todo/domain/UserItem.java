/*
  UserItem.java

  Created by John Boyer on Oct 16, 2017
  Copyright (c) 2017 Rodax Software, Inc.
  
  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodaxsoft.todo.security.ApplicationAuthentication;

/**
 * User table class
 * @author John Boyer
 *
 */
@DynamoDBTable(tableName = "User")
public class UserItem implements ApplicationAuthentication {
	private class ProfileImpl implements Profile {

		@Override
		@JsonGetter("avatar_url")
		public String getAvatarUrl() {
			return null;
		}

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public String getName() {
			return name;
		}
		
	}
	
	@DynamoDBHashKey
	@DynamoDBAttribute(attributeName = "email")
	private String email;
	
	@DynamoDBAttribute(attributeName = "name")
	private String name;

	@DynamoDBAttribute(attributeName = "password")
	private String password;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return The user's unique email address
	 */
	@JsonIgnore
	@DynamoDBIgnore
	public String getId() {
		return email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@JsonIgnore
	@DynamoDBIgnore
	public Profile getProfile() {
		return new ProfileImpl();
	}
	
	@Override
	@JsonIgnore
	@DynamoDBIgnore
	public String getUsername() {
		return email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@DynamoDBIgnore
	public String toString() {
		return "UserItem [email=" + email + ", name=" + name + ", password=*****]";
	}

}
