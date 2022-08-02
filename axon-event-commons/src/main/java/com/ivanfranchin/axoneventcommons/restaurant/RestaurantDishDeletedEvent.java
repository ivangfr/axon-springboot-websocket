package com.ivanfranchin.axoneventcommons.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDishDeletedEvent implements RestaurantEvent {

    private String restaurantId;
    private String dishId;
}
