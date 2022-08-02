package com.ivanfranchin.restaurantservice.mapper;

import com.ivanfranchin.restaurantservice.model.Dish;
import com.ivanfranchin.restaurantservice.model.Order;
import com.ivanfranchin.restaurantservice.model.Restaurant;
import com.ivanfranchin.restaurantservice.rest.dto.DishDto;
import com.ivanfranchin.restaurantservice.rest.dto.RestaurantDto;
import com.ivanfranchin.restaurantservice.rest.dto.RestaurantOrderDto;
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
