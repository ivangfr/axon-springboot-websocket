package com.ivanfranchin.restaurantservice.rest.dto;

import com.ivanfranchin.restaurantservice.model.Dish;

import java.math.BigDecimal;

public record DishResponse(String restaurantId, String dishId, String dishName, BigDecimal dishPrice) {

    public static DishResponse from(Dish dish) {
        return new DishResponse(
                dish.getRestaurant().getId(),
                dish.getId(),
                dish.getName(),
                dish.getPrice());
    }
}
