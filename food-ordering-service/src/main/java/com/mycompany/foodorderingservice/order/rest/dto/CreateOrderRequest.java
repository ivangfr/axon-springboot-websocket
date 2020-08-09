package com.mycompany.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
