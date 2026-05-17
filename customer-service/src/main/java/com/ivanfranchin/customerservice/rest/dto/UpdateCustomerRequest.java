package com.ivanfranchin.customerservice.rest.dto;

public record UpdateCustomerRequest(
        String name,
        String address) {
}
