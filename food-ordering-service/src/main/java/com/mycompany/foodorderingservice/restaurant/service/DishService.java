package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.model.Dish;

public interface DishService {

    Dish validateAndGetDish(String restaurantId, String dishId);

    Dish saveDish(Dish dish);

    void deleteDish(Dish dish);

}
