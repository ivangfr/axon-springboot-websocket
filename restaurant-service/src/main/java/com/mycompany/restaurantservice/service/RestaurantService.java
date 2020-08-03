package com.mycompany.restaurantservice.service;

import com.mycompany.restaurantservice.model.Dish;
import com.mycompany.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantService {

    Restaurant validateAndGetRestaurant(String restaurantId);

    Dish validateAndGetRestaurantDish(String restaurantId, String dishId);

}
