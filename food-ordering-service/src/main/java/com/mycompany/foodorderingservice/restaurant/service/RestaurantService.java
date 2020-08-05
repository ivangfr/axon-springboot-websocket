package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.model.Restaurant;

public interface RestaurantService {

    Restaurant validateAndGetRestaurant(String id);

}
