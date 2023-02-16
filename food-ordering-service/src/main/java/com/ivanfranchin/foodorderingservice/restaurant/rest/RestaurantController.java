package com.ivanfranchin.foodorderingservice.restaurant.rest;

import com.ivanfranchin.foodorderingservice.restaurant.mapper.RestaurantMapper;
import com.ivanfranchin.foodorderingservice.restaurant.rest.dto.RestaurantResponse;
import com.ivanfranchin.foodorderingservice.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @GetMapping
    public List<RestaurantResponse> getRestaurants() {
        return restaurantService.getRestaurants().stream().map(restaurantMapper::toRestaurantResponse).toList();
    }
}
