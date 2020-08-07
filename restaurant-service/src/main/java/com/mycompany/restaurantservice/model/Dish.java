package com.mycompany.restaurantservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    private String id;
    private String name;
    private BigDecimal price;

}
