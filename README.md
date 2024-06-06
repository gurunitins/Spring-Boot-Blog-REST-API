[![Build Status](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API.svg?branch=development)](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API) [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=coma123_Spring-Boot-Blog-REST-API&metric=alert_status)](https://sonarcloud.io/dashboard?id=coma123_Spring-Boot-Blog-REST-API) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3706/badge)](https://bestpractices.coreinfrastructure.org/projects/3706)

# Spring Boot, MySQL, Spring Security, JWT, JPA, Rest API

Build Restful CRUD API for a blog using Spring Boot, Mysql, JPA and Hibernate.

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/coma123/Spring-Boot-Blog-REST-API.git
```

**2. Create Mysql database**

```bash
create database blogapi
```

- run `src/main/resources/blogapi.sql`

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`
+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Run the app using maven**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url              | Decription | Sample Valid Request Body | 
|--------|------------------|------------|---------------------------|
| POST   | /api/auth/signup | Sign up    | [JSON](#signup)           |
| POST   | /api/auth/signin | Log in     | [JSON](#signin)           |

### Users

| Method | Url                                  | Description                                                                                             | Sample Valid Request Body |
|--------|--------------------------------------|---------------------------------------------------------------------------------------------------------|---------------------------|
| GET    | /api/users/me                        | Get logged in userEntity profile                                                                        |                           |
| GET    | /api/users/{username}/profile        | Get userEntity profile by username                                                                      |                           |
| GET    | /api/users/{username}/posts          | Get posts created by userEntity                                                                         |                           |
| GET    | /api/users/{username}/albums         | Get albums created by userEntity                                                                        |                           |
| GET    | /api/users/checkUsernameAvailability | Check if username is available to register                                                              |                           |
| GET    | /api/users/checkEmailAvailability    | Check if email is available to register                                                                 |                           |
| POST   | /api/users                           | Add userEntity (Only for admins)                                                                        | [JSON](#usercreate)       |
| PUT    | /api/users/{username}                | Update userEntity (If profile belongs to logged in userEntity or logged in userEntity is admin)         | [JSON](#userupdate)       |
| DELETE | /api/users/{username}                | Delete userEntity (For logged in userEntity or admin)                                                   |                           |
| PUT    | /api/users/{username}/giveAdmin      | Give admin role to userEntity (only for admins)                                                         |                           |
| PUT    | /api/users/{username}/TakeAdmin      | Take admin role from userEntity (only for admins)                                                       |                           |
| PUT    | /api/users/setOrUpdateInfo           | Update userEntity profile (If profile belongs to logged in userEntity or logged in userEntity is admin) | [JSON](#userinfoupdate)   |

### Posts

| Method | Url             | Description                                                                            | Sample Valid Request Body |
|--------|-----------------|----------------------------------------------------------------------------------------|---------------------------|
| GET    | /api/posts      | Get all posts                                                                          |                           |
| GET    | /api/posts/{id} | Get post by id                                                                         |                           |
| POST   | /api/posts      | Create new post (By logged in userEntity)                                              | [JSON](#postcreate)       |
| PUT    | /api/posts/{id} | Update post (If post belongs to logged in userEntity or logged in userEntity is admin) | [JSON](#postupdate)       |
| DELETE | /api/posts/{id} | Delete post (If post belongs to logged in userEntity or logged in userEntity is admin) |                           |

### Comments

| Method | Url                               | Description                                                                                                                               | Sample Valid Request Body |
|--------|-----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|
| GET    | /api/posts/{postId}/comments      | Get all comments which belongs to post with id = postId                                                                                   |                           |
| GET    | /api/posts/{postId}/comments/{id} | Get comment by id if it belongs to post with id = postId                                                                                  |                           |
| POST   | /api/posts/{postId}/comments      | Create new comment for post with id = postId (By logged in userEntity)                                                                    | [JSON](#commentcreate)    |
| PUT    | /api/posts/{postId}/comments/{id} | Update comment by id if it belongs to post with id = postId (If comment belongs to logged in userEntity or logged in userEntity is admin) | [JSON](#commentupdate)    |
| DELETE | /api/posts/{postId}/comments/{id} | Delete comment by id if it belongs to post with id = postId (If comment belongs to logged in userEntity or logged in userEntity is admin) |                           |

### Albums

| Method | Url                     | Description                                                                              | Sample Valid Request Body |
|--------|-------------------------|------------------------------------------------------------------------------------------|---------------------------|
| GET    | /api/albums             | Get all albums                                                                           |                           |
| GET    | /api/albums/{id}        | Get album by id                                                                          |                           |
| POST   | /api/albums             | Create new album (By logged in userEntity)                                               | [JSON](#albumcreate)      |
| PUT    | /api/albums/{id}        | Update album (If album belongs to logged in userEntity or logged in userEntity is admin) | [JSON](#albumupdate)      |
| DELETE | /api/albums/{id}        | Delete album (If album belongs to logged in userEntity or logged in userEntity is admin) |                           |
| GET    | /api/albums/{id}/photos | Get all photos which belongs to album with id = id                                       |                           |

### Photos

| Method | Url              | Description                                                                              | Sample Valid Request Body |
|--------|------------------|------------------------------------------------------------------------------------------|---------------------------|
| GET    | /api/photos      | Get all photos                                                                           |                           |
| GET    | /api/photos/{id} | Get photo by id                                                                          |                           |
| POST   | /api/photos      | Create new photo (By logged in userEntity)                                               | [JSON](#photocreate)      |
| PUT    | /api/photos/{id} | Update photo (If photo belongs to logged in userEntity or logged in userEntity is admin) | [JSON](#photoupdate)      |
| DELETE | /api/photos/{id} | Delete photo (If photo belongs to logged in userEntity or logged in userEntity is admin) |                           |

### Todos

| Method | Url                               | Description                                                                   | Sample Valid Request Body |
|--------|-----------------------------------|-------------------------------------------------------------------------------|---------------------------|
| GET    | /api/todoEntities                 | Get all todoEntities which belongs to logged in userEntity                    |                           |
| GET    | /api/todoEntities/{id}            | Get todoEntity by id (If todoEntity belongs to logged in userEntity)          |                           |
| POST   | /api/todoEntities                 | Create new todoEntity (By logged in userEntity)                               | [JSON](#todocreate)       |
| PUT    | /api/todoEntities/{id}            | Update todoEntity (If todoEntity belongs to logged in userEntity)             | [JSON](#todoupdate)       |
| DELETE | /api/todoEntities/{id}            | Delete todoEntity (If todoEntity belongs to logged in userEntity)             |                           |
| PUT    | /api/todoEntities/{id}/complete   | Mark todoEntity as complete (If todoEntity belongs to logged in userEntity)   |                           |
| PUT    | /api/todoEntities/{id}/unComplete | Mark todoEntity as uncomplete (If todoEntity belongs to logged in userEntity) |                           |

Test them using postman or any other rest client.

## Sample Valid JSON Request Bodys

##### <a id="signup">Sign Up -> /api/auth/signup</a>

```json
{
	"firstName": "Leanne",
	"lastName": "Graham",
	"username": "leanne",
	"password": "password",
	"email": "leanne.graham@gmail.com"
}
```

##### <a id="signin">Log In -> /api/auth/signin</a>

```json
{
	"usernameOrEmail": "leanne",
	"password": "password"
}
```

##### <a id="usercreate">Create User -> /api/users</a>

```json
{
	"firstName": "Ervin",
	"lastName": "Howell",
	"username": "ervin",
	"password": "password",
	"email": "ervin.howell@gmail.com",
	"address": {
		"street": "Victor Plains",
		"suite": "Suite 879",
		"city": "Wisokyburgh",
		"zipcode": "90566-7771",
		"geo": {
			"lat": "-43.9509",
			"lng": "-34.4618"
		}
	},
	"phone": "010-692-6593 x09125",
	"website": "http://erwinhowell.com",
	"company": {
		"name": "Deckow-Crist",
		"catchPhrase": "Proactive didactic contingency",
		"bs": "synergize scalable supply-chains"
	}
}
```

##### <a id="userupdate">Update User -> /api/users/{username}</a>

```json
{
	"firstName": "Ervin",
	"lastName": "Howell",
	"username": "ervin",
	"password": "updatedpassword",
	"email": "ervin.howell@gmail.com",
	"address": {
		"street": "Victor Plains",
		"suite": "Suite 879",
		"city": "Wisokyburgh",
		"zipcode": "90566-7771",
		"geo": {
			"lat": "-43.9509",
			"lng": "-34.4618"
		}
	},
	"phone": "010-692-6593 x09125",
	"website": "http://erwinhowell.com",
	"company": {
		"name": "Deckow-Crist",
		"catchPhrase": "Proactive didactic contingency",
		"bs": "synergize scalable supply-chains"
	}
}
```

##### <a id="userinfoupdate">Update User Profile -> /api/users/setOrUpdateInfo</a>

```json
{
	"street": "Douglas Extension",
	"suite": "Suite 847",
	"city": "McKenziehaven",
	"zipcode": "59590-4157",
	"companyName": "Romaguera-Jacobson",
	"catchPhrase": "Face to face bifurcated interface",
	"bs": "e-enable strategic applications",
	"website": "http://ramiro.info",
	"phone": "1-463-123-4447",
	"lat": "-68.6102",
	"lng": "-47.0653"
}
```

##### <a id="postcreate">Create Post -> /api/posts</a>

```json
{
	"title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
	"body": "quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto"
}
```

##### <a id="postupdate">Update Post -> /api/posts/{id}</a>

```json
{
	"title": "UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED",
	"body": "UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED "
}
```

##### <a id="commentcreate">Create Comment -> /api/posts/{postId}/comments</a>

```json
{
	"body": "laudantium enim quasi est quidem magnam voluptate ipsam eos tempora quo necessitatibus dolor quam autem quasi reiciendis et nam sapiente accusantium"
}
```

##### <a id="commentupdate">Update Comment -> /api/posts/{postId}/comments/{id}</a>

```json
{
	"body": "UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED "
}
```

##### <a id="albumcreate">Create Album -> /api/albums</a>

```json
{
	"title": "quidem molestiae enim"
}
```

##### <a id="albumupdate">Update Album -> /api/albums/{id}</a>

```json
{
	"title": "quidem molestiae enim UPDATED"
}
```

##### <a id="photocreate">Create Photo -> /api/photos</a>

```json
{
	"title": "accusamus beatae ad facilis cum similique qui sunt",
	"url": "https://via.placeholder.com/600/92c952",
	"thumbnailUrl": "https://via.placeholder.com/150/92c952",
	"albumId": 2
}
```

##### <a id="photoupdate">Update Photo -> /api/photos{id}</a>

```json
{
	"title": "accusamus beatae ad facilis ",
	"url": "https://via.placeholder.com/600/771796",
	"thumbnailUrl": "https://via.placeholder.com/150/771796",
	"albumId": 4
}
```

##### <a id="todocreate">Create Todo -> /api/todoEntities</a>

```json
{
	"title": "delectus aut autem",
	"completed": false
}
```

##### <a id="todoupdate">Update Todo -> /api/todoEntities{id}</a>

```json
{
	"title": "delectus aut autem Updated",
	"completed": true
}
```

![segment](https://api.segment.io/v1/pixel/track?data=ewogICJ3cml0ZUtleSI6ICJwcDJuOTU4VU1NT21NR090MWJXS0JQd0tFNkcydW51OCIsCiAgInVzZXJJZCI6ICIxMjNibG9nYXBpMTIzIiwKICAiZXZlbnQiOiAiQmxvZ0FwaSB2aXNpdGVkIiwKICAicHJvcGVydGllcyI6IHsKICAgICJzdWJqZWN0IjogIkJsb2dBcGkgdmlzaXRlZCIsCiAgICAiZW1haWwiOiAiY29tcy5zcHVyc0BnbWFpbC5jb20iCiAgfQp9)
