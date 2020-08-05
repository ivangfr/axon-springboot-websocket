package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.model.Dish;

public interface DishService {

    Dish validateAndGetDish(String id);

}
