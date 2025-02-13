package com.ivanfranchin.restaurantservice.rest.dto;

import com.ivanfranchin.restaurantservice.model.Restaurant;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record RestaurantResponse(String id, String name, Set<Dish> dishes) {

    public record Dish(String id, String name, BigDecimal price) {
    }

    public static RestaurantResponse from(Restaurant restaurant) {
        Set<Dish> dishes = restaurant.getDishes()
                .stream()
                .map(dish -> new Dish(
                        dish.getId(),
                        dish.getName(),
                        dish.getPrice()))
                .collect(Collectors.toSet());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                dishes);
    }
}
