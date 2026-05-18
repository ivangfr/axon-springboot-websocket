package com.ivanfranchin.foodorderingservice.restaurant.rest;

import com.ivanfranchin.foodorderingservice.restaurant.rest.dto.RestaurantResponse;
import com.ivanfranchin.foodorderingservice.restaurant.service.RestaurantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

  private final RestaurantService restaurantService;

  @GetMapping
  public List<RestaurantResponse> getRestaurants() {
    return restaurantService.getRestaurants().stream().map(RestaurantResponse::from).toList();
  }
}
