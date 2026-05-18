package com.ivanfranchin.restaurantservice.repository;

import com.ivanfranchin.restaurantservice.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

  List<Order> findByRestaurantIdOrderByCreatedAtDesc(String restaurantId);
}
