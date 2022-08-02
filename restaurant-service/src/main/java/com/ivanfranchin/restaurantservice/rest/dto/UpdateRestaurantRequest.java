package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateRestaurantRequest {

    @Schema(example = "McDonald's")
    private String name;
}
