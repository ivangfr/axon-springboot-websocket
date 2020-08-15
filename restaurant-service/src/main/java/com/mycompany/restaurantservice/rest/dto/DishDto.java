package com.mycompany.restaurantservice.rest.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishDto {

    private String restaurantId;
    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;

}
