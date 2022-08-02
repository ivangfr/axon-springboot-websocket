package com.ivanfranchin.restaurantservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(String id) {
        super(String.format("Restaurant with id '%s' not found", id));
    }
}
