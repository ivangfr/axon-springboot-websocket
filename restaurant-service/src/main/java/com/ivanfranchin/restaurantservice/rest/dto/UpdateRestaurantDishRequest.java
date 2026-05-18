package com.ivanfranchin.restaurantservice.rest.dto;

import java.math.BigDecimal;

public record UpdateRestaurantDishRequest(String name, BigDecimal price) {}
