package com.ivanfranchin.foodorderingservice.restaurant.service;

import com.ivanfranchin.foodorderingservice.restaurant.exception.DishNotFoundException;
import com.ivanfranchin.foodorderingservice.restaurant.exception.RestaurantNotFoundException;
import com.ivanfranchin.foodorderingservice.restaurant.model.Dish;
import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import com.ivanfranchin.foodorderingservice.restaurant.repository.RestaurantRepository;
import java.util.List;
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
        .filter(d -> d.getId().equals(dishId))
        .findAny()
        .orElseThrow(() -> new DishNotFoundException(dishId));
  }

  @Override
  public List<Restaurant> getRestaurants() {
    return restaurantRepository.findAll();
  }
}
