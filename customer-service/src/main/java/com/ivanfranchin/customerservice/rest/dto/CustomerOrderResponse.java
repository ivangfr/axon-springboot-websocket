package com.ivanfranchin.customerservice.rest.dto;

import com.ivanfranchin.customerservice.model.Order;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record CustomerOrderResponse(String id,
                                    String restaurantName,
                                    String status,
                                    BigDecimal total,
                                    ZonedDateTime createdAt,
                                    Set<OrderItem> items) {

    public record OrderItem(String dishName, BigDecimal dishPrice, Short quantity) {
    }

    public static CustomerOrderResponse from(Order order) {
        Set<OrderItem> items = order.getItems()
                .stream()
                .map(orderItem -> new OrderItem(
                        orderItem.getDishName(),
                        orderItem.getDishPrice(),
                        orderItem.getQuantity()))
                .collect(Collectors.toSet());

        return new CustomerOrderResponse(
                order.getId(),
                order.getRestaurantName(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items);
    }
}
