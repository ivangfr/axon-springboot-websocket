package com.mycompany.axoneventcommons.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent implements OrderEvent {

    private String orderId;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;
    private String status;
    private BigDecimal total;
    private Set<OrderItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        private String dishId;
        private String dishName;
        private BigDecimal dishPrice;
        private Short quantity;
    }

}
