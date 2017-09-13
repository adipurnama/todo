/*
  ApplicationUser.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodaxsoft.todo.security.Credentials;

/**
 * ApplicationUser class
 */
@Entity
public class ApplicationUser implements Credentials {
	
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

	private String email;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String password;
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	public Profile getProfile() {
		return new ProfileImpl();
	}

	@Override
	public String getUsername() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
