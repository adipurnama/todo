/*
  DynamoDBUserMapper.java

  The MIT License (MIT)

  Created by John Boyer on Oct 19, 2017
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
package com.rodaxsoft.todo.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.rodaxsoft.todo.domain.UserItem;

/**
 * @author johndev
 *
 */
@Repository
public class DynamoDBUserMapper  {
	
	private AmazonDynamoDB client;
	
	@Autowired
	public DynamoDBUserMapper(DynamoDBProperties props) {
		client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(props.getHost(), props.getRegion())).build();
	}
	
	public boolean exists(UserItem user) {
		return findByEmail(user.getEmail()) != null;
	}
	
	public UserItem createUser(UserItem user) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		mapper.save(user);
		return user;
	}

	public UserItem findByEmail(String email) {
		return new DynamoDBMapper(client).load(UserItem.class, email);	
	}
	
	public void deleteUser(UserItem user) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		mapper.delete(user);
	}
}
