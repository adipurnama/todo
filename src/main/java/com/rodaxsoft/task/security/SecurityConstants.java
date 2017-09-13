/*
  SecurityConstants.java

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

/**
 * SecurityConstants interface
 */
public interface SecurityConstants {
	/**
	 * Secret string
	 */
    String SECRET = "748475bd-4afe-4c20-9681-8985423c076c";
    /**
     * Expiration time in milliseconds
     */
    long EXPIRATION_TIME = 600_000; // 10 minutes
    /**
     * Token prefix, e.g., <code>Bearer</code>
     */
    String TOKEN_PREFIX = "";
    /**
     * Header string, e.g., <code>Authorization</code> or<code> x-access-token</code>
     */
    String HEADER_STRING = "x-access-token";
    String COOKIE_STRING = "ACCESS_TOKEN";
    /**
     * Sign-up URL
     */
    String SIGN_UP_URL = "/users";
    /**
     * Login URL
     */
    String LOGIN_URL = "/access-tokens";
    
	/**
	 * Max cookie age is 600 s or 10 min.
	 */
	int MAX_COOKIE_AGE = (int) (EXPIRATION_TIME/1000);
}
