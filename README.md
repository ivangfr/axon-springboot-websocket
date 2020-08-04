# axon-kafka-springboot-react-keycloak

The goal of this project is play with [`Axon`](https://axoniq.io/). For it, we will implement some [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) applications.

## Project Architecture

TODO

## Applications

- ### customer-service

  `Spring Boot` application that exposes a REST API to manage `Customers`. The data is stored in `MySQL`.

- ### restaurant-service

  `Spring Boot` application that exposes a REST API to manage `Restaurants`. The data is stored in `MongoDB`.
  
- ### food-ordering-service

  `Spring Boot` application that exposes a REST API to manage `Orders`. The data is stored in `MySQL`.

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Start environment

- Open a terminal and inside `axon-kafka-springboot-react-keycloak` root folder run
  ```
  docker-compose up -d
  ```

- Wait a bit until `MySQL` is `Up (healthy)`. You can check it by running
  ```
  docker-compose ps
  ```

## Running Applications

Inside `axon-kafka-springboot-react-keycloak` root folder, run the following commands in different terminals.

- **axon-event-commons**
  ```
  ./mvnw clean install --projects axon-event-commons
  ```

- **customer-service**
  ```
  ./mvnw clean spring-boot:run --projects customer-service -Dspring-boot.run.jvmArguments="-Dserver.port=9080"
  ```

- **restaurant-service**
  ```
  ./mvnw clean spring-boot:run --projects restaurant-service -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
  ```

- **food-ordering-service**
  ```
  ./mvnw clean spring-boot:run --projects food-ordering-service -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
  ```

## Useful Commands & Links

- **MySQL**
  ```
  docker exec -it mysql-customer mysql -uroot -psecret --database customerdb
  docker exec -it mysql-food-ordering mysql -uroot -psecret --database foodorderingdb
  ```
  
- **MongoDB**
  ```
  docker exec -it mongodb mongo
  use restaurantdb
  db.restaurants.find()
  ```

- **Kafka Topics UI**

  `Kafka Topics UI` can be accessed at http://localhost:8085

## Shutdown

- To stop the applications, go to the terminals where they are running and press `Ctrl+C`
- To stop and remove docker-compose containers, networks and volumes, make sure you are inside `axon-kafka-springboot-react-keycloak` root folder and run
  ```
  docker-compose down -v
  ```

## References

- https://sgitario.github.io/axon-by-example/
- https://blog.nebrass.fr/playing-with-cqrs-and-event-sourcing-in-spring-boot-and-axon/