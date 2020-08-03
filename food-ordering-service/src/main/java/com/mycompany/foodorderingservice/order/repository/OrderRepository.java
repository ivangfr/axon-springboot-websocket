package com.mycompany.foodorderingservice.order.repository;

import com.mycompany.foodorderingservice.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
