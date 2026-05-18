package com.ivanfranchin.restaurantservice.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AddRestaurantRequest(@NotBlank String name) {}
