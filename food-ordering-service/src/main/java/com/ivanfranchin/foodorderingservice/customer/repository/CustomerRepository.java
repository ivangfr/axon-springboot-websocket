package com.ivanfranchin.foodorderingservice.customer.repository;

import com.ivanfranchin.foodorderingservice.customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
