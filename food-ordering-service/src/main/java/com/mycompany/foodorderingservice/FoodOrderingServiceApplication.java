package com.mycompany.foodorderingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class FoodOrderingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodOrderingServiceApplication.class, args);
    }

}
