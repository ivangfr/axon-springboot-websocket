package com.mycompany.foodorderingservice.customer.repository;

import com.mycompany.foodorderingservice.customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
