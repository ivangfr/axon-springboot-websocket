package com.ivanfranchin.restaurantservice.repository;

import com.ivanfranchin.restaurantservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByRestaurantIdOrderByCreatedAtDesc(String restaurantId);
}
