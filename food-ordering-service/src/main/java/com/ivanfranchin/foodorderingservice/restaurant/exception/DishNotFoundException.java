package com.ivanfranchin.foodorderingservice.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DishNotFoundException extends RuntimeException {

    public DishNotFoundException(String dishId) {
        super(String.format("Dish with id '%s' not found", dishId));
    }
}
