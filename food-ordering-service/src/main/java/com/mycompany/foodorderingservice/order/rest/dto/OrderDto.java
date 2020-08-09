package com.mycompany.foodorderingservice.order.rest.dto;

import com.mycompany.foodorderingservice.order.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
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
    private Set<OrderItem> items;

    @Data
    public static class OrderItem {
        private String dishId;
        private String dishName;
        private BigDecimal dishPrice;
        private Short quantity;
    }

}
