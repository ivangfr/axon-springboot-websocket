package com.mycompany.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class AddRestaurantDishRequest {

    @Schema(example = "Pizza Margherita 25cm")
    @NotBlank
    private String name;

    @Schema(example = "6.99")
    @Positive
    private Float price;

}
