# Todo API

Todo is a task management service that enables registered users to easily manage their tasks. Each task has a *name* as well as other optional values including *due date*, *status*, and *description*. The next version will include *rank*, so that tasks can be ordered by priority.

The Todo API is implemented as a [Spring Boot](https://projects.spring.io/spring-boot/) app running an embedded version of Tomcat. For demonstration purposes, the data store is a [HSQLDB](http://hsqldb.org) in-memory database accessed via [Spring JPA](https://projects.spring.io/spring-data-jpa/). It uses [JSON Web Tokens (JWT)](https://jwt.io/) for authentication.

# Table of Contents

1. [Build and Run](#build-and-run)
2. [Reference](#reference)
3. [Contributors](#contributors)
4. [License](#license)

# Build and Run

To build and run the project in place type: `$ gradle bootRun`

To just build the project type: `$ gradle build`

For information on installing Gradle go to https://gradle.org/install

# Reference
To create and manage tasks, first create a user. See [signup](#signup-user).

Note: All dates are specified as strings, i.e., `yyyy-MM-dd` or `yyyy-MM-dd'T'hh:mm:ss`.

## User

Method | HTTP Requests | Description
------------ | ------------- |-------
*[signup](#insert-user)* | `POST /users`| Create a user
*[profile](#view)* | `GET /me` | View current user
*[login](#login)* |`POST/access-tokens` | Login user
*[logout](#logout)* |`DELETE/access-tokens` | Logout user
*[refresh](#refresh-token)* |`POST /access-tokens/refresh` |Refresh user token

## Tasks

Method | HTTP Requests | Description
------------ | ------------- |-------
*[insert](#insert-task)* | `POST /tasks` | Create a task
*[list](#get-tasks)* | `GET /tasks`| Returns tasks
*[update](#update-task)* |`PUT /tasks/:id` | Update a task
*[delete](#delete-task)* |`DELETE /tasks/:id` | Delete a task

---

### Signup User

#### Request
`POST /users`

#### Header
No header required.

#### Parameters
No parameters required.

#### Request Body
All elements are required.

	{
  	  "email": string,
  	  "name": string,
  	  "password": string
	}

#### Response
If successful, returns a JWT JSON:

	{
  	  "jwt": string,
  	  "refresh_token": string
	}

Clients should securely persist the access token, i.e., the `jwt` element, and the `refresh_token`. The access token is required as Cookie value for most of the API methods with the exception of signup and login.

---

### Profile

#### Request
`GET /me`

#### Header

	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body
No body required.

#### Response
If successful, returns a JSON user:

	{
  	  "email": string,
  	  "name": string
	}

---

### Login

#### Request
`POST/access-tokens`

#### Header
	Content-Type: application/json

#### Parameters
No parameters required.

#### Request Body

	{
  	  "email": string,
  	  "name": string
	}

#### Response
If successful, returns a JSON Web Token:

	{
  	  "jwt": string,
  	  "refresh_token": string
	}

---

### Logout

#### Request
`DELETE/access-tokens`

#### Header
	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body
	{
  	  "refresh_token": string
	}

#### Response
No response, if successful.

---

### Refresh Token

#### Request
`POST /access-tokens/refresh`

#### Header
	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body
	{
  	  "refresh_token": string
	}

#### Response
If successful, returns a JSON Web Token:

	{
  	  "jwt": string
	}

---

### Insert Task

#### Request
`POST /tasks`

#### Header
	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body

	{
  	  "completed": string (yyyy-MM-dd),
  	  "description": string,
  	  "due": date string (yyyy-MM-dd),
  	  "status": string (open | completed),
 	  "title": string <required>
	}

#### Response
If successful, returns JSON task:

	{
  	  "completed": string (yyyy-MM-dd),
  	  "created": string (yyyy-MM-dd'T'hh:mm:ss),
  	  "description": string,
  	  "due": string (yyyy-MM-dd),
  	  "id": number,
  	  "modified": string (yyyy-MM-dd'T'hh:mm:ss),
  	  "status": string (open | completed),
  	  "title": string
	}

---

### Get Tasks

#### Request
`GET /tasks`

#### Header
	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body
No body required

#### Response
If successful, returns an array of tasks:

	[{
  	   "completed": string (yyyy-MM-dd),
  	   "created": string (yyyy-MM-dd'T'hh:mm:ss),
  	   "description": string,
  	   "due": string (yyyy-MM-dd),
  	   "id": number,
  	   "modified": string (yyyy-MM-dd'T'hh:mm:ss),
  	   "status": string (open | completed),
  	   "title": string
	}, {
  	     "completed": string (yyyy-MM-dd),
  	     "created": string (yyyy-MM-dd'T'hh:mm:ss),
  	     "description": string,
  	     "due": string (yyyy-MM-dd),
  	     "id": number,
  	     "modified": string (yyyy-MM-dd'T'hh:mm:ss),
  	     "status": string (open | completed),
  	     "title": string
	},...
	]

---

### Update Task

#### Request
`PUT /tasks/:id`

#### Header
	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body

	{
  	  "completed": string (yyyy-MM-dd),
  	  "description": string | null,
  	  "due": date string (yyyy-MM-dd),
  	  "status": string (open | completed),
 	    "title": string <required>
	}

#### Response
If successful, returns JSON task:

	{
  	  "completed": string (yyyy-MM-dd),
  	  "created": string (yyyy-MM-dd'T'hh:mm:ss),
  	  "description": string,
  	  "due": string (yyyy-MM-dd),
  	  "id": number,
  	  "modified": string (yyyy-MM-dd'T'hh:mm:ss),
  	  "status": string (open | completed),
  	  "title": string
	}

---

### Delete Task

#### Request
`DELETE /tasks/:id`

#### Header
	Content-Type: application/json
	Cookie: ACCESS_TOKEN=<JWT access token string>

#### Parameters
No parameters required.

#### Request Body
No body required.

#### Response
No response, if successful.

# Contributors

To reach a milestone for a major release, we'd like contributions for the following:
* Add task for ranking and priority sorting
* Implement task lists or categories
* Add support for SQL or NoSQL persistent data store

Contributions can be made by following these steps:

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

If you have any questions, please don't hesitate to contact me at john@rodaxsoft.com.

# License
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
