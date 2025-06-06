package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddRestaurantRequest(@Schema(example = "PizzaHut") @NotBlank String name) {
}
