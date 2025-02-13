package com.ivanfranchin.restaurantservice.rest.dto;

import com.ivanfranchin.restaurantservice.model.Order;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record RestaurantOrderResponse(String id,
                                      String customerName,
                                      String customerAddress,
                                      String status,
                                      BigDecimal total,
                                      ZonedDateTime createdAt,
                                      Set<OrderItem> items) {

    public record OrderItem(String dishId, String dishName, BigDecimal dishPrice, Short quantity) {
    }

    public static RestaurantOrderResponse from(Order order) {
        Set<OrderItem> items = order.getItems()
                .stream()
                .map(orderItem -> new OrderItem(
                        orderItem.getDishId(),
                        orderItem.getDishName(),
                        orderItem.getDishPrice(),
                        orderItem.getQuantity()))
                .collect(Collectors.toSet());

        return new RestaurantOrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerAddress(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items);
    }
}
