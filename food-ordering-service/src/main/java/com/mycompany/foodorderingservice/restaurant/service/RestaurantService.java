package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.model.Dish;
import com.mycompany.foodorderingservice.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant validateAndGetRestaurant(String id);

    Dish validateAndGetRestaurantDish(String restaurantId, String dishId);

    List<Restaurant> getRestaurants();
}
