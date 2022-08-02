package com.ivanfranchin.restaurantservice.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRestaurantDishQuery {

    private String restaurantId;
    private String dishId;
}
