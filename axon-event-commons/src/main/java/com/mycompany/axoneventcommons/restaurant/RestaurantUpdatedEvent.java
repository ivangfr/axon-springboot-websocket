package com.mycompany.axoneventcommons.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdatedEvent implements RestaurantEvent {

    private String id;
    private String name;

}
