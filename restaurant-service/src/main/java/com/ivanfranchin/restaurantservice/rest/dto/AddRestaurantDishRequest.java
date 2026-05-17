package com.ivanfranchin.restaurantservice.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddRestaurantDishRequest(
        @NotBlank String name,
        @Positive BigDecimal price) {
}
