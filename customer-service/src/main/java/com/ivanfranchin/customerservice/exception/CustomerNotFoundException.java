package com.ivanfranchin.customerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String id) {
        super(String.format("Customer with id '%s' not found", id));
    }
}
