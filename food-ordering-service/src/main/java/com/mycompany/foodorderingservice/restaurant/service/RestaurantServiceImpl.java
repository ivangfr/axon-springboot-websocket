package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.exception.DishNotFoundException;
import com.mycompany.foodorderingservice.restaurant.exception.RestaurantNotFoundException;
import com.mycompany.foodorderingservice.restaurant.model.Dish;
import com.mycompany.foodorderingservice.restaurant.model.Restaurant;
import com.mycompany.foodorderingservice.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant validateAndGetRestaurant(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    @Override
    public Dish validateAndGetRestaurantDish(String restaurantId, String dishId) {
        return validateAndGetRestaurant(restaurantId).getDishes().stream()
                .filter(d -> d.getId().equals(dishId)).findAny().orElseThrow(() -> new DishNotFoundException(dishId));
    }
}
