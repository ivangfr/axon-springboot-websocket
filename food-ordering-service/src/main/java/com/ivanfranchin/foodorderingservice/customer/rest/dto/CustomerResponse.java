package com.ivanfranchin.foodorderingservice.customer.rest.dto;

import com.ivanfranchin.foodorderingservice.customer.model.Customer;

public record CustomerResponse(String id, String name, String address) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getAddress());
    }
}
