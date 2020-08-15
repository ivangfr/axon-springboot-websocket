package com.mycompany.restaurantservice.mapper;

import com.mycompany.restaurantservice.model.Dish;
import com.mycompany.restaurantservice.model.Order;
import com.mycompany.restaurantservice.model.Restaurant;
import com.mycompany.restaurantservice.rest.dto.DishDto;
import com.mycompany.restaurantservice.rest.dto.RestaurantDto;
import com.mycompany.restaurantservice.rest.dto.RestaurantOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDto toRestaurantDto(Restaurant restaurant);

    RestaurantOrderDto toRestaurantOrderDto(Order order);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "dishId", source = "id")
    @Mapping(target = "dishName", source = "name")
    @Mapping(target = "dishPrice", source = "price")
    DishDto toDishDto(Dish dish);

}
