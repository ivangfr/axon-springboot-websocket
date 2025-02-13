package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddRestaurantDishRequest(
        @Schema(example = "Pizza Margherita 25cm") @NotBlank String name,
        @Schema(example = "6.99") @Positive BigDecimal price) {
}
