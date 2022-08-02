package com.ivanfranchin.restaurantservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DishNotFoundException extends RuntimeException {

    public DishNotFoundException(String restaurantId, String dishId) {
        super(String.format("Dish with id '%s' in restaurant '%s' not found", dishId, restaurantId));
    }
}
