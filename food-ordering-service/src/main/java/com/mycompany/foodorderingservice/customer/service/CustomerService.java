package com.mycompany.foodorderingservice.customer.service;

import com.mycompany.foodorderingservice.customer.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer validateAndGetCustomer(String id);

    List<Customer> getCustomers();

}
