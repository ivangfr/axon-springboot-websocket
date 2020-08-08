package com.mycompany.customerservice.repository;

import com.mycompany.customerservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
