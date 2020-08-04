package com.mycompany.axoneventcommons.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDishUpdatedEvent implements RestaurantEvent {

    private String restaurantId;
    private String dishId;
    private String dishName;
    private Float dishPrice;

}
