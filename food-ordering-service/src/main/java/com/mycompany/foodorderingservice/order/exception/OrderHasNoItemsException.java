package com.mycompany.foodorderingservice.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderHasNoItemsException extends RuntimeException {

    public OrderHasNoItemsException(String id) {
        super(String.format("Order with id '%s' has no items", id));
    }

}
