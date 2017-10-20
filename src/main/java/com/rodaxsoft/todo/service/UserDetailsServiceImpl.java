/*
  UserDetailsServiceImpl.java

  Created by John Boyer on Sep 5, 2017
  Copyright (c) 2017 Rodax Software, Inc.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/
package com.rodaxsoft.todo.service;

import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rodaxsoft.todo.data.DynamoDBUserMapper;
import com.rodaxsoft.todo.domain.UserItem;
import com.rodaxsoft.todo.exception.ResourceNotFoundException;

/**
 * UserDetailsServiceImpl class
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
    private DynamoDBUserMapper applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserItem applicationUser = applicationUserRepository.findByEmail(username);
        if (applicationUser == null) {
            throw new ResourceNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
