package com.ivanfranchin.restaurantservice.rest.dto;

import java.math.BigDecimal;

public record DishResponse(String restaurantId, String dishId, String dishName, BigDecimal dishPrice) {
}
