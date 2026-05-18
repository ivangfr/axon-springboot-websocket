package com.ivanfranchin.foodorderingservice;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mongodb.MongoDBContainer;

public interface MongoDBTestcontainers {

  @Container @ServiceConnection
  MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.2.5");
}
