package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddRestaurantRequest {

    @Schema(example = "PizzaHut")
    @NotBlank
    private String name;
}
