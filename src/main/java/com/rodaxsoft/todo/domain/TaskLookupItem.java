/*
  TaskLookupItem.java

  Created by John Boyer on Oct 16, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * TaskLookup table class
 * @author John Boyer
 *
 */
@DynamoDBTable(tableName = "TaskLookup")
public class TaskLookupItem {
	
	@DynamoDBAttribute(attributeName = "taskIdentifiers")
	private Set<String> taskIdentifiers;
	@DynamoDBHashKey(attributeName = "userId")
	private String userId;
	/**
	 * @return the taskIdentifiers
	 */
	public Set<String> getTaskIdentifiers() {
		return taskIdentifiers;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param taskIdentifiers the taskIdentifiers to set
	 */
	public void setTaskIdentifiers(Set<String> taskIdentifiers) {
		this.taskIdentifiers = taskIdentifiers;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaskLookupItem [taskIdentifiers=" + taskIdentifiers + ", userId=" + userId + "]";
	}

}
