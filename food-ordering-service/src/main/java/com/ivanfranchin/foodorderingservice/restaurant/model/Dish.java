package com.ivanfranchin.foodorderingservice.restaurant.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Dish {

    private String id;
    private String name;
    private BigDecimal price;
}
