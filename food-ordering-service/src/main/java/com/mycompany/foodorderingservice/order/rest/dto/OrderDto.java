package com.mycompany.foodorderingservice.order.rest.dto;

import com.mycompany.foodorderingservice.order.model.OrderStatus;
import lombok.Data;

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
    private Float total;
    private Set<OrderItem> items;

    @Data
    public static class OrderItem {
        private String id;
        private String dishId;
        private String dishName;
        private Float dishPrice;
        private Short quantity;
    }

}
