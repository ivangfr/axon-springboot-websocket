package com.mycompany.foodorderingservice.restaurant.rest;

import com.mycompany.foodorderingservice.restaurant.mapper.RestaurantMapper;
import com.mycompany.foodorderingservice.restaurant.rest.dto.RestaurantDto;
import com.mycompany.foodorderingservice.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @GetMapping
    public List<RestaurantDto> getRestaurants() {
        return restaurantService.getRestaurants().stream()
                .map(restaurantMapper::toRestaurantDto)
                .collect(Collectors.toList());
    }

}
