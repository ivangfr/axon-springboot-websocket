package com.ivanfranchin.foodorderingservice.order.rest.dto;

import com.ivanfranchin.foodorderingservice.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public record OrderResponse(String id, String customerId, String customerName, String customerAddress, String restaurantId,
                            String restaurantName, OrderStatus status, BigDecimal total, ZonedDateTime createdAt,
                            Set<OrderItem> items) {

    public record OrderItem(String dishId, String dishName, BigDecimal dishPrice, Short quantity) {
    }
}
