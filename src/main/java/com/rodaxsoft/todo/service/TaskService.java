/*
  TaskService.java

  Created by John Boyer on Oct 19, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.service;

import java.util.List;

import com.rodaxsoft.todo.domain.TaskItem;

/**
 * TaskItem service interface
 * @author John Boyer
 *
 */
public interface TaskService {
	/**
	 * Creates a task in a data store
	 * @param task The task to create
	 * @return
	 */
	TaskItem createTask(TaskItem task);
	/**
	 * Deletes the task for the given <code>id</code>.
	 * @param id The task id
	 */
	void deleteTask(String id);
	/**
	 * Determines if the task exists in the data store
	 * @param id The task id
	 * @return A boolean value of <code>true</code>; otherwise, <code>false</code>.
	 */
	boolean exists(String id);
	/**
	 * Determines if the task exists in the data store
	 * @param task The task
	 * @return A boolean value of <code>true</code>; otherwise, <code>false</code>.
	 */
	boolean exists(TaskItem task);
	/**
	 * @param userId
	 * @return
	 */
	List<TaskItem> getTasks(String userId);
	/**
	 * Determines if the user has tasks
	 * @param userId The user id
	 * @return <code>true</code> if the user has tasks; otherwise, <code>null</code>.
	 */
	boolean hasTasks(String userId);
	/**
	 * Updates the task
	 * @param task The task to update
	 * @return The updated task
	 */
	TaskItem updateTask(TaskItem task);
}