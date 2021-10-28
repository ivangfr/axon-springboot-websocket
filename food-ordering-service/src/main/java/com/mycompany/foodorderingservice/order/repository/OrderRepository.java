package com.mycompany.foodorderingservice.order.repository;

import com.mycompany.foodorderingservice.order.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
