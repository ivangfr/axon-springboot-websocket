package com.ivanfranchin.restaurantservice.mapper;

import com.ivanfranchin.restaurantservice.model.Dish;
import com.ivanfranchin.restaurantservice.model.Order;
import com.ivanfranchin.restaurantservice.model.Restaurant;
import com.ivanfranchin.restaurantservice.rest.dto.DishResponse;
import com.ivanfranchin.restaurantservice.rest.dto.RestaurantResponse;
import com.ivanfranchin.restaurantservice.rest.dto.RestaurantOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    RestaurantOrderResponse toRestaurantOrderResponse(Order order);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "dishId", source = "id")
    @Mapping(target = "dishName", source = "name")
    @Mapping(target = "dishPrice", source = "price")
    DishResponse toDishResponse(Dish dish);
}
