/*
  DynamoDBTaskMapper.java

  Created by John Boyer on Oct 17, 2017
  Copyright (c) 2017 Rodax Software, Inc.
  
  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.rodaxsoft.todo.domain.TaskItem;
import com.rodaxsoft.todo.domain.TaskLookupItem;
import com.rodaxsoft.todo.exception.DuplicateItemException;
import com.rodaxsoft.todo.exception.ResourceNotFoundException;
import com.rodaxsoft.todo.exception.ValidationException;


/**
 * Task mapper class handles interaction with DynamoDB
 * @author John Boyer
 *
 */
@Repository
public class DynamoDBTaskMapper {

	private static void saveTaskLookupItem(TaskItem task, DynamoDBMapper mapper) {
		// First check for an entry
		TaskLookupItem lookup;
		Set<String> ids;

		lookup = mapper.load(TaskLookupItem.class, task.getUserId());
		if (null == lookup) {
			lookup = new TaskLookupItem();
			lookup.setUserId(task.getUserId());
			ids = new HashSet<>();
		} else {
			ids = lookup.getTaskIdentifiers();
		}

		ids.add(task.getId());
		lookup.setTaskIdentifiers(ids);
		// Save the task in the TaskLookup table
		mapper.save(lookup);
	}

	private AmazonDynamoDB client;

	@Autowired
	public DynamoDBTaskMapper(DynamoDBProperties props) {
		client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(props.getHost(), props.getRegion())).build();
	}
	
	public TaskItem createTask(TaskItem task) {
		if(exists(task)) {
			throw new DuplicateItemException("Task already exists: " + task.getId());
		}
		
		if(null == task.getUserId()) {
			throw new ValidationException("userId cannot be null");
		}
		
		// Create the new tasks in Task table
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		mapper.save(task);

		// Save the task id in the lookup table
		saveTaskLookupItem(task, mapper);

		return task;
	}
	
	public void deleteTaskById(String id) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		TaskItem taskItem = mapper.load(TaskItem.class, id);
		if(taskItem != null) {
			
			//Remove the task id from the TaskLookup table
			TaskLookupItem lookup = mapper.load(TaskLookupItem.class, taskItem.getUserId());
			if(lookup != null) {
				Set<String> taskIds = lookup.getTaskIdentifiers();
				if(taskIds.remove(taskItem.getId())) {
					if(taskIds.isEmpty()) {
						mapper.delete(lookup);
					} else {
						lookup.setTaskIdentifiers(taskIds);
						mapper.save(lookup);
					}
				}
			} 
			
			//Remove the task from 
			mapper.delete(taskItem);			
		} else {
			throw new ResourceNotFoundException("Task not found: " + id);
		}
		
	}
	
	/**
	 * @param id The task id
	 * @return
	 */
	public boolean exists(String id) {
		return id != null && new DynamoDBMapper(client).load(TaskItem.class, id) != null;
	}
	
	public boolean exists(TaskItem task) {
		return exists(task.getId());
	}

	public TaskItem find(TaskItem task) {
		return new DynamoDBMapper(client).load(task);
	}
	
	public TaskItem findById(String id) {
		return new DynamoDBMapper(client).load(TaskItem.class, id);
	}
	
	/**
	 * Determines if the user has tasks
	 * @param userId The user's id
	 * @return
	 */
	public boolean hasTasks(String userId) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		return  mapper.load(TaskLookupItem.class, userId) != null;
	}
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskItem >findByUserId(String userId) {
		//Get the saved ids
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		TaskLookupItem lookup = mapper.load(TaskLookupItem.class, userId);	
		if(null == lookup) {
			throw new ResourceNotFoundException("User has no tasks: " + userId);
		}
		
		Set<String> savedIds = lookup.getTaskIdentifiers();
		
		//Batch load by building the list of key-pairs
		List<KeyPair> keyPairs = new ArrayList<>();
		for (String key : savedIds) {
			keyPairs.add(new KeyPair().withHashKey(key));
		}

		Map<Class<?>, List<KeyPair>> tables = new HashMap<>();
		tables.put(TaskItem.class, keyPairs);
		Map<String, List<Object>> objectItems = mapper.batchLoad(tables);
		List<TaskItem> tasks = new ArrayList<>();
		if(objectItems != null && objectItems.get("Task") != null) {
			tasks = ((List<TaskItem>)(List)objectItems.get("Task"));
		}
				
		return tasks;
	}

	public TaskItem updateTask(TaskItem task) {
		if(exists(task)) {
			DynamoDBMapper mapper = new DynamoDBMapper(client);
			mapper.save(task);			
		} else {
			throw new ResourceNotFoundException("Task item not found: " + task.getId());
		}

		return task;
	}

}
