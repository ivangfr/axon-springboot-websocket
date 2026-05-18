package com.ivanfranchin.customerservice.repository;

import com.ivanfranchin.customerservice.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

  List<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId);
}
