package com.ivanfranchin.restaurantservice;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;

public interface PostgreSQLTestcontainers {

  @Container @ServiceConnection
  PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18.0");
}
