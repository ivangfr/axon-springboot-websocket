package com.mycompany.foodorderingservice.order.exception;

import com.mycompany.foodorderingservice.order.model.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderInvalidOperationException extends RuntimeException {

    public OrderInvalidOperationException(String id, OrderStatus status) {
        super(String.format("Order with id '%s' has status %s", id, status));
    }

}
