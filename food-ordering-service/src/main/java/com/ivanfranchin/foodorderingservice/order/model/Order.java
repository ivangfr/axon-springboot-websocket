package com.ivanfranchin.foodorderingservice.order.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private OrderStatus status;
    private BigDecimal total = BigDecimal.ZERO;
    private ZonedDateTime createdAt;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;
    private Set<OrderItem> items;
}
