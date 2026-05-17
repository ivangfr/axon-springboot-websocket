package com.ivanfranchin.customerservice;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mysql.MySQLContainer;

public interface MySQLTestcontainers {

  @Container @ServiceConnection
  MySQLContainer mySQLContainer =
      new MySQLContainer("mysql:9.6.0")
          .withUrlParam("characterEncoding", "UTF-8")
          .withUrlParam("serverTimezone", "UTC");
}
