# F L W R S P O T
Java Spring Boot API for Flower sightings

### Description
Application contains a flower listing where registered & logged in users
can manage their flower sightings by adding/deleting them and their likes for them.

## Starting the application

### Locally
#### Prerequisites
- make sure you have git, Java Development Kit, Maven and PostgreSQL installed
- PostgreSQL Database (default values: db name=`flwrspot` & db port=5432)

In root folder of the application start these commands:
````
mvn clean install
java -jar services/monolith/target/monolith-0.0.1-SNAPSHOT.jar  --spring.config.location=classpath:/application.yml
````
Application should be running at this point at *http://localhost:8080*

--- 
To change database name, username, password url or port, navigate to file:
```
services/monolith/services/monolith/src/main/resources/application.yaml
```
and change properties:
```
  datasource:
    url: jdbc:postgresql://localhost:<DB_PORT>/flwrspot
    username: <USERNAME>
    password: <PASSWORD>
```
After changing properties, re-run application.

### APIs
When the application is running, Swagger API documentation is available at url:
```
http://localhost:8080/swagger-ui.html
```
This is the list of accessible APIs:

#### For unregistered users

##### REGISTER
POST `/api/v1/auth/register` with request body:
```
{
  "email": "string",
  "password": "string",
  "username": "string"
}
```
-----
##### LOGIN
POST `/api/v1/auth/login` with request body:
```
{
  "password": "string",
  "username": "string"
}
```
----
##### LIST FLOWERS
GET `api/v1/flower/list` 

----
#### For registered users

##### ADD NEW FLOWER SIGHTING
POST `/api/v1/sighting` with request body:
```
{
  "longitude": 0,
  "latitude": 0,
  "imageUrl": "string",
  "flower": {
    "description": "string",
    "flowerSightings": [
      null
    ],
    "id": "string",
    "imageUrl": "string",
    "name": "string"
  }
}
```
##### DELETE SPECIFIC USER SIGHTING (sighting id provided)
DELETE `/api/v1/sighting/{id}`

----

##### DELETE ALL USER SIGHTINGS
DELETE `/api/v1/sighting/`

----

##### LIST ALL USER SIGHTINGS (flower id provided)
GET `/api/v1/sighting/{id}`

----

#### ADD LIKE TO A SIGHTING (sighting id provided in url)
POST `/api/v1/like/{id}`

----

#### DELETE A LIKE (like id provided in url)
DELETE `/api/v1/like/{id}`

----
`
#### DELETE ALL USER LIKES
DELETE `/api/v1/like`

----

### Tests
Unit test are run by default on `mvn clean install` command. <br/> <br/>
They can be found in packages: <br/>
`common` - EmailValidatorTest <br/>
`core` in `external.quotes` parent package - QuoteManagerTest <br/>
`jpa` in `knowledge-base` parent package - JpaKnowledgeBaseTest <br/>
`api` in `ws` parent package - AuthServiceTest <br/>
`ws-spring-mvc` in `ws` parent package - Controllers tests <br/>
