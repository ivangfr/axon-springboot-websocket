package com.mycompany.foodorderingservice.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderItemNotFoundException extends RuntimeException {

    public OrderItemNotFoundException(String orderId, String itemId) {
        super(String.format("Item with id '%s' in order '%s' not found", itemId, orderId));
    }

}
