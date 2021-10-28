package com.mycompany.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddRestaurantRequest {

    @Schema(example = "PizzaHut")
    @NotBlank
    private String name;
}
