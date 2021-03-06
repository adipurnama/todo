/*
  TaskItem.java

  Created by John Boyer on Oct 16, 2017
  Copyright (c) 2017 Rodax Software, Inc.
  
  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.domain;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedDefault;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodaxsoft.todo.exception.ValidationException;

/**
 * Task table class
 * @author John Boyer
 *
 */
@DynamoDBTable(tableName = "Task")
public class TaskItem {
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DynamoDBAttribute(attributeName = "completed")
	private Date completed;

	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
	@DynamoDBAttribute(attributeName = "created")
	@DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
	private Date created;

	@DynamoDBAttribute(attributeName = "description")
	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DynamoDBAttribute(attributeName = "due")
	private Date due;

	@DynamoDBAutoGeneratedKey
	@DynamoDBHashKey(attributeName = "id")
	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
	@DynamoDBAttribute(attributeName = "modified")
	@DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
	private Date modified;

	@DynamoDBAttribute(attributeName = "status")
	@DynamoDBAutoGeneratedDefault("open")
	private String status = "open";

	@DynamoDBAttribute(attributeName = "title")
	private String title;

	@JsonIgnore
	@DynamoDBAttribute(attributeName = "userId")
	private String userId;

	/**
	 * @return the completed
	 */
	public Date getCompleted() {
		return completed;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the due
	 */
	public Date getDue() {
		return due;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param due the due to set
	 */
	public void setDue(Date due) {
		this.due = due;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	@DynamoDBIgnore
	public void setStatus(TaskStatus status) {
		switch (status) {
		case COMPLETED:
		case OPEN:
			break;
		default:
			throw new ValidationException("Invalid status");
		}		
		
		this.status = status.toString();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	@DynamoDBIgnore
	public String toString() {
		return "TaskItem [completed=" + completed + ", created=" + created + ", description=" + description + ", due="
				+ due + ", id=" + id + ", modified=" + modified + ", status=" + status + ", title=" + title
				+ ", userId=" + userId + "]";
	}

}
