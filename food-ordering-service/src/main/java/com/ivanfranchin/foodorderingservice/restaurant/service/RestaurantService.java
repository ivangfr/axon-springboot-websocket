package com.ivanfranchin.foodorderingservice.restaurant.service;

import com.ivanfranchin.foodorderingservice.restaurant.model.Dish;
import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant validateAndGetRestaurant(String id);

    Dish validateAndGetRestaurantDish(String restaurantId, String dishId);

    List<Restaurant> getRestaurants();
}
