package com.ivanfranchin.foodorderingservice.order.rest.dto;

import com.ivanfranchin.foodorderingservice.order.model.Order;
import com.ivanfranchin.foodorderingservice.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderResponse(String id,
                            String customerId,
                            String customerName,
                            String customerAddress,
                            String restaurantId,
                            String restaurantName,
                            OrderStatus status,
                            BigDecimal total,
                            ZonedDateTime createdAt,
                            Set<OrderItem> items) {

    public record OrderItem(String dishId, String dishName, BigDecimal dishPrice, Short quantity) {
    }

    public static OrderResponse from(Order order) {
        Set<OrderItem> items = order.getItems()
                .stream()
                .map(orderItem -> new OrderItem(
                        orderItem.getDishId(),
                        orderItem.getDishName(),
                        orderItem.getDishPrice(),
                        orderItem.getQuantity()))
                .collect(Collectors.toSet());

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getCustomerAddress(),
                order.getRestaurantId(),
                order.getRestaurantName(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items);
    }
}
