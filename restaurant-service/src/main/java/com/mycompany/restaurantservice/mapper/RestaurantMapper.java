package com.mycompany.restaurantservice.mapper;

import com.mycompany.restaurantservice.model.Order;
import com.mycompany.restaurantservice.model.Restaurant;
import com.mycompany.restaurantservice.rest.dto.RestaurantDto;
import com.mycompany.restaurantservice.rest.dto.RestaurantOrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDto toRestaurantDto(Restaurant restaurant);

    RestaurantOrderDto toRestaurantOrderDto(Order order);

}
