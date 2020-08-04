package com.mycompany.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateRestaurantDishRequest {

    @Schema(example = "Pizza Margherita 35cm")
    private String name;

    @Schema(example = "7.99")
    private Float price;

}
