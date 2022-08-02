package com.ivanfranchin.foodorderingservice.customer.service;

import com.ivanfranchin.foodorderingservice.customer.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer validateAndGetCustomer(String id);

    List<Customer> getCustomers();
}
