package com.mycompany.foodorderingservice.order.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OpenOrderRequest {

    private UUID customerId;
    private UUID restaurantId;

}
