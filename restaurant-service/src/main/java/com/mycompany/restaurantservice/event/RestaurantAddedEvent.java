package com.mycompany.restaurantservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAddedEvent {

    private String id;
    private String name;

}
