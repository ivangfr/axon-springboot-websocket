package com.mycompany.restaurantservice.repository;

import com.mycompany.restaurantservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByRestaurantIdOrderByCreatedAtDesc(String restaurantId);

}
