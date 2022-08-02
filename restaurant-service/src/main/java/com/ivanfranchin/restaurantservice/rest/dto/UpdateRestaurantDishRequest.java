package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateRestaurantDishRequest {

    @Schema(example = "Pizza Margherita 35cm")
    private String name;

    @Schema(example = "7.99")
    private BigDecimal price;
}
