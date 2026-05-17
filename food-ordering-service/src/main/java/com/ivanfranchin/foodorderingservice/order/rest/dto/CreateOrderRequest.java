package com.ivanfranchin.foodorderingservice.order.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotNull UUID restaurantId,
        @NotNull @NotEmpty Set<OrderItemRequest> items) {

    public record OrderItemRequest(
            UUID dishId,
            Short quantity) {
    }
}
