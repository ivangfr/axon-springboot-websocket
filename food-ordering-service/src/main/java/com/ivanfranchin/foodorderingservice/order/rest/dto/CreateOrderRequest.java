package com.ivanfranchin.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateOrderRequest(
        UUID customerId,
        UUID restaurantId,
        @NotNull @NotEmpty Set<OrderItemRequest> items) {

    public record OrderItemRequest(
            UUID dishId,
            @Schema(example = "2") Short quantity) {
    }
}
