package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddRestaurantDishRequest {

    @Schema(example = "Pizza Margherita 25cm")
    @NotBlank
    private String name;

    @Schema(example = "6.99")
    @Positive
    private BigDecimal price;
}
