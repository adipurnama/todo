# Todo API

## User

Method | HTTP Requests | Description 
------------ | ------------- |-------
*[insert](#insert-user)* | `POST /users`| Create a user 
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

### Insert User

#### Request
`POST /users`

#### Header
No header required.

#### Parameters
No parameters required.

#### Request Body

	{
  	  "email": string,
  	  "name": string,
  	  "password": string
	}
	
#### Response
If successful, returns a JSON Web Token:

	{
  	  "jwt": string,
  	  "refresh_token": string
	}
	
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
