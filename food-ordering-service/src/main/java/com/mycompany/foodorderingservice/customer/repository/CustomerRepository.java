package com.mycompany.foodorderingservice.customer.repository;

import com.mycompany.foodorderingservice.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
