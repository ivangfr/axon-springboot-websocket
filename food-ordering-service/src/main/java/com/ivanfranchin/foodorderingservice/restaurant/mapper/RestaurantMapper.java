package com.ivanfranchin.foodorderingservice.restaurant.mapper;

import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import com.ivanfranchin.foodorderingservice.restaurant.rest.dto.RestaurantDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDto toRestaurantDto(Restaurant restaurant);
}
