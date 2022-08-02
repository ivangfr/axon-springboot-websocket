package com.ivanfranchin.foodorderingservice.restaurant.mapper;

import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import com.ivanfranchin.foodorderingservice.restaurant.rest.dto.RestaurantResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
}
