# axon-springboot-websocket

The goal of this project is play with [`Axon`](https://axoniq.io/). For it, we will implement a `food-ordering` app that consists of three [`Spring Boot`](https://docs.spring.io/spring-boot/index.html) applications: `customer-service`, `restaurant-service` and `food-ordering-service`. These services were implemented with [`CQRS`](https://martinfowler.com/bliki/CQRS.html) and [`Event Sourcing`](https://martinfowler.com/eaaDev/EventSourcing.html) in mind so, in order to achieve it, we used [`Axon Framework`](https://www.axoniq.io/products/axon-framework). The three services are connected to `axon-server` that is the [`Event Store`](https://en.wikipedia.org/wiki/Event_store) and `Message Routing` solution used.

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Project Diagram

![project-diagram](documentation/project-diagram.jpeg)

## Applications

- ### customer-service

  `Spring Boot` application that exposes a REST API to manage `Customers`. It also has a UI implemented using `Javascript`, `JQuery` and `Semantic UI`.
  
  ![customer-service](documentation/customer-service.jpeg)
  
  `customer-service` was implemented using `Axon Framework`. Everytime a customer is added, updated or deleted, the service emits the respective event, i.e, `CustomerAddedEvent`, `CustomerUpdatedEvent` or `CustomerDeletedEvent`.
  
  `customer-service` uses `MySQL` to store customer's data. Besides, it listens to order events, collects the order information that it needs and stores them in an order table present in its own database, so that it doesn't need to call another service to get this information.

- ### restaurant-service

  `Spring Boot` application that exposes a REST API to manage `Restaurants`. It also has a UI implemented using `Javascript`, `JQuery` and `Semantic UI`.
  
  ![restaurant-service](documentation/restaurant-service.jpeg)
  
  `restaurant-service` was implemented using `Axon Framework`. Everytime a restaurant is added, updated or deleted, the service emits the respective event, i.e, `RestaurantAddedEvent`, `RestaurantUpdatedEvent` or `RestaurantDeletedEvent`. The same applies to the restaurant dishes, whose events are: `RestaurantDishAddedEvent`, `RestaurantDishUpdatedEvent` or `RestaurantDishDeletedEvent` 
  
  `restaurant-service` uses `PostgreSQL` to store restaurant/dish data. Besides, it listens to order events, collects the order information that it needs and stores them in an order table present in its own database, so that it doesn't need to call another service to get this information.
  
- ### food-ordering-service

  `Spring Boot` application that exposes a REST API to manage `Orders`. It has a UI implemented using `Javascript`, `JQuery` and `Semantic UI`.
  
  ![food-ordering-service](documentation/food-ordering-service.jpeg)
  
  `food-ordering-service` was implemented using `Axon Framework`. Everytime an order is created, the service emits the respective event, i.e, `OrderCreatedEvent`.
  
  `food-ordering-service` uses `MongoDB` to store order data. Besides, it listens to customer and restaurant/dish events, collects the information that it needs and stores them in a customer or restaurant/dish table present in this own database, so that it doesn't need to call another service to get this information.

- ### axon-event-commons

  `Maven` project where all events mentioned above are defined. It generates a JAR file that is added as a dependency in the `pom.xml` of `customer-service`, `restaurant-service` and `food-ordering-service`.

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)

## Start Environment

- Open a terminal and inside `axon-springboot-websocket` root folder run:
  ```
  docker compose up -d
  ```

- Wait for Docker containers to be up and running. To check it, run:
  ```
  docker compose ps
  ```

## Running applications with Maven

Inside `axon-springboot-websocket` root folder, run the following commands in different terminals:

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

## Running applications as Docker containers

- ### Build Docker images

  - In a terminal, make sure you are in `axon-springboot-websocket` root folder;
  - Run the following script to build the Docker images:
    - JVM
      ```
      ./docker-build.sh
      ```
    - Native (it's not implemented yet)
      ```
      ./docker-build.sh native
      ```

- ### Environment Variables

  - **customer-service**

    | Environment Variable | Description                                                       |
    |----------------------|-------------------------------------------------------------------|
    | `MYSQL_HOST`         | Specify host of the `MySQL` database to use (default `localhost`) |
    | `MYSQL_PORT`         | Specify port of the `MySQL` database to use (default `3306`)      |
    | `AXON_SERVER_HOST`   | Specify host of the `Axon Server` to use (default `localhost`)    |
    | `AXON_SERVER_PORT`   | Specify port of the `Axon Server` to use (default `8124`)         |

  - **restaurant-service**

    | Environment Variable | Description                                                          |
    |----------------------|----------------------------------------------------------------------|
    | `POSTGRES_HOST`      | Specify host of the `Postgres` database to use (default `localhost`) |
    | `POSTGRES_PORT`      | Specify port of the `Postgres` database to use (default `5432`)      |
    | `AXON_SERVER_HOST`   | Specify host of the `Axon Server` to use (default `localhost`)       |
    | `AXON_SERVER_PORT`   | Specify port of the `Axon Server` to use (default `8124`)            |

  - **food-ordering-service**

    | Environment Variable | Description                                                       |
    |----------------------|-------------------------------------------------------------------|
    | `MONGODB_HOST`       | Specify host of the `Mongo` database to use (default `localhost`) |
    | `MONGODB_PORT`       | Specify port of the `Mongo` database to use (default `27017`)     |
    | `AXON_SERVER_HOST`   | Specify host of the `Axon Server` to use (default `localhost`)    |
    | `AXON_SERVER_PORT`   | Specify port of the `Axon Server` to use (default `8124`)         |

- ### Start Docker containers

  - In a terminal, make sure you are inside `axon-springboot-websocket` root folder;
  - Run following command:
    ```
    ./start-apps.sh
    ```

## Application URLs

| Application           | URL                   | Swagger                               |
|-----------------------|-----------------------|---------------------------------------|
| customer-service      | http://localhost:9080 | http://localhost:9080/swagger-ui.html |
| restaurant-service    | http://localhost:9081 | http://localhost:9081/swagger-ui.html |
| food-ordering-service | http://localhost:9082 | http://localhost:9082/swagger-ui.html |

## Demo

The GIF below shows a user creating a customer in `customer-service` UI. Then, in `restaurant-service` UI, he creates a restaurant and adds a dish. Finally, using `food-ordering-service` UI, he submits an order using the customer and restaurant/dish created. Note that as soon as a customer or restaurant/dish is created, an event is sent and, the consumer of this event updates its UI in realtime using WebSockets.

![demo](documentation/demo.gif)

## Useful Commands & Links

- **Axon Server**
  
  `Axon Server` dashboard can be accessed at http://localhost:8024
  
  ![axon-server](documentation/axon-server.jpeg)

- **MySQL**
  ```
  docker exec -it -e MYSQL_PWD=secret mysql mysql -uroot --database customerdb
  
  SELECT * FROM customers;
  SELECT * FROM orders;
  ```
  > Type `exit` to exit

- **PostgreSQL**
  ```
  docker exec -it postgres psql -U postgres -d restaurantdb
  
  SELECT * FROM restaurants;
  SELECT * FROM dishes;
  SELECT * FROM orders;
  ```
  > Type `\q` to exit
  
- **MongoDB**
  ```
  docker exec -it mongodb mongo foodorderingdb
  
  db.customers.find()
  db.restaurants.find()
  db.orders.find()
  ```
  > Type `exit` to exit

## Shutdown

- To stop applications:
    - If you start them with `Maven`, go to the terminals where they are running and press `Ctrl+C`;
    - If you start them as Docker containers, make sure you are inside `axon-springboot-websocket` root folder and run the following script:
      ```
      ./stop-apps.sh
      ```
- To stop and remove docker compose containers, network and volumes, go to a terminal and, inside `axon-springboot-websocket` root folder, run the command below:
  ```
  docker compose down -v
  ```

## Cleanup

To remove the docker images created by this project, go to a terminal and, inside `axon-springboot-websocket` root folder, run the following script:
```
./remove-docker-images.sh
```

## References

- https://sgitario.github.io/axon-by-example/
- https://blog.nebrass.fr/playing-with-cqrs-and-event-sourcing-in-spring-boot-and-axon/
