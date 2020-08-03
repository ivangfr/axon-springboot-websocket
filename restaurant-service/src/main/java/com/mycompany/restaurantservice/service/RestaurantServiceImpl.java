package com.mycompany.restaurantservice.service;

import com.mycompany.restaurantservice.exception.DishNotFoundException;
import com.mycompany.restaurantservice.exception.RestaurantNotFoundException;
import com.mycompany.restaurantservice.model.Dish;
import com.mycompany.restaurantservice.model.Restaurant;
import com.mycompany.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant validateAndGetRestaurant(String restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    @Override
    public Dish validateAndGetRestaurantDish(String restaurantId, String dishId) {
        return validateAndGetRestaurant(restaurantId).getDishes().stream()
                .filter(d -> d.getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new DishNotFoundException(restaurantId, dishId));
    }

}
