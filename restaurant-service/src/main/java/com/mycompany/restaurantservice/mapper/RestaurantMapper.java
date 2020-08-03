package com.mycompany.restaurantservice.mapper;

import com.mycompany.restaurantservice.model.Restaurant;
import com.mycompany.restaurantservice.rest.dto.RestaurantDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDto toRestaurantDto(Restaurant restaurant);

}
