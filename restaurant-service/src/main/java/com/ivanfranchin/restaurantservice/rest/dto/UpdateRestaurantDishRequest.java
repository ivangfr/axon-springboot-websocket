package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record UpdateRestaurantDishRequest(
        @Schema(example = "Pizza Margherita 35cm") String name,
        @Schema(example = "7.99") BigDecimal price) {
}
