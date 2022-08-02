package com.ivanfranchin.foodorderingservice.order.rest.dto;

import com.ivanfranchin.foodorderingservice.order.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class OrderDto {

    private String id;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;
    private OrderStatus status;
    private BigDecimal total;
    private ZonedDateTime createdAt;
    private Set<OrderItem> items;

    @Data
    public static class OrderItem {
        private String dishId;
        private String dishName;
        private BigDecimal dishPrice;
        private Short quantity;
    }
}
