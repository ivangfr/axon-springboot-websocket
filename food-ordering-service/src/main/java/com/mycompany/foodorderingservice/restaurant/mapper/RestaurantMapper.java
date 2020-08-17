package com.mycompany.foodorderingservice.restaurant.mapper;

import com.mycompany.foodorderingservice.restaurant.model.Restaurant;
import com.mycompany.foodorderingservice.restaurant.rest.dto.RestaurantDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDto toRestaurantDto(Restaurant restaurant);

}
