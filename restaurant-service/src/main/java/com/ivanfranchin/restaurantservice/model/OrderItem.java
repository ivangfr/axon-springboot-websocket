package com.ivanfranchin.restaurantservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {

    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;
    private Short quantity;
}
