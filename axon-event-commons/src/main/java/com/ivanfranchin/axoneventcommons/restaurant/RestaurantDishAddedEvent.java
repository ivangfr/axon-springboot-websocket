package com.ivanfranchin.axoneventcommons.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDishAddedEvent implements RestaurantEvent {

    private String restaurantId;
    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;
}
