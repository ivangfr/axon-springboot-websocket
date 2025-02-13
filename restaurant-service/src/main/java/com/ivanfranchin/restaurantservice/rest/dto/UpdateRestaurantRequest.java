package com.ivanfranchin.restaurantservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateRestaurantRequest(@Schema(example = "McDonald's") String name) {
}
