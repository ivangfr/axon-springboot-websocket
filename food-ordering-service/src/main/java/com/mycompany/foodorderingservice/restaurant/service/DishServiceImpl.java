package com.mycompany.foodorderingservice.restaurant.service;

import com.mycompany.foodorderingservice.restaurant.exception.DishNotFoundException;
import com.mycompany.foodorderingservice.restaurant.model.Dish;
import com.mycompany.foodorderingservice.restaurant.model.DishPk;
import com.mycompany.foodorderingservice.restaurant.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Override
    public Dish validateAndGetDish(String restaurantId, String dishId) {
        DishPk dishPk = new DishPk(dishId, restaurantId);
        return dishRepository.findById(dishPk).orElseThrow(() -> new DishNotFoundException(restaurantId, dishId));
    }

    @Override
    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public void deleteDish(Dish dish) {
        dishRepository.delete(dish);
    }
}
