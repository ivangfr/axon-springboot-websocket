package com.ivanfranchin.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CreateOrderRequest {

    private UUID customerId;
    private UUID restaurantId;

    @NotNull
    @NotEmpty
    private Set<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {

        private UUID dishId;

        @Schema(example = "2")
        private Short quantity;
    }
}
