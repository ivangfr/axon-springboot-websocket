package com.ivanfranchin.axoneventcommons.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAddedEvent implements RestaurantEvent {

    private String id;
    private String name;
}
