package com.ivanfranchin.foodorderingservice.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;
    private Short quantity;
}
