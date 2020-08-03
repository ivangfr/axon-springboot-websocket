package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.model.Restaurant;

public interface RestaurantService {

    Restaurant validateAndGetRestaurant(String id);

    Restaurant saveRestaurant(Restaurant restaurant);

    void deleteRestaurant(Restaurant restaurant);

}
