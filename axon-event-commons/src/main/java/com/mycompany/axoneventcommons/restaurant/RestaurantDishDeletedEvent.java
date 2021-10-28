package com.mycompany.axoneventcommons.restaurant;

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
