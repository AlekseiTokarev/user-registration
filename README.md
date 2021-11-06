# user-registration
Kotlin + Spring Boot REST API sample project for user registration.

### Postgres DB initialization
1. Create DB and user:  
  `DROP ROLE IF EXISTS varo; 
   CREATE ROLE varo LOGIN PASSWORD 'varo';`

    `CREATE DATABASE backend_test WITH OWNER varo;`

2. execute DDL queries form `db-init.sql` 

### Running app locally
1. build a jar: `./gradlew clean bootJar`
2. start a spring boot app: `java -jar ./build/libs/user-registration.jar`

### curl samples
1. creating/replacing a user with particular id
   `curl -X POST --location "http://localhost:8080/api/users" \
   -H "Content-Type: application/json" \
   -d "{
   \"id\": 10,
   \"firstName\": \"John\",
   \"lastName\": \"Random\",
   \"email\": \"test@example.com\"
   }"`

2. creating a new user
   `curl -X POST --location "http://localhost:8080/api/users" \
   -H "Content-Type: application/json" \
   -d "{
   \"firstName\": \"John\",
   \"lastName\": \"Random\",
   \"email\": \"test@example.com\"
   }"`

3. getting a user details 
   `curl -X GET --location "http://localhost:8080/api/users/2/details"`

4. checking if user exists
   `curl -X GET --location "http://localhost:8080/api/users/existsByEmail?email=test@example.com"`

5. updating user address
    `curl -X POST --location "http://localhost:8080/api/users/10/address" \
   -H "Content-Type: application/json" \
   -d "{
   \"line1\": \"123 Example drive\",
   \"line2\": \"1233\",
   \"city\": \"Coppell\",
   \"state\": \"TX\",
   \"zip\": \"75019\"
   }"`
 
6. deleting a user
   `curl -X DELETE --location "http://localhost:8080/api/users/2"`

7. getting list of users is not implemented due to time constraint
`curl -X GET --location "http://localhost:8080/api/users?limit=5&offset=0"`
