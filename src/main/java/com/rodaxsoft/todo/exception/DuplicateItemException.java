/*
  DuplicateItemException.java

  Created by John Boyer on Oct 18, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DuplicateItemException class
 * @author John Boyer
 *
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateItemException extends RuntimeException {


	private static final long serialVersionUID = 2507132716717363749L;

	/**
	 * @param message
	 */
	public DuplicateItemException(String message) {
		super(message);
	}

	
}
