package com.mycompany.restaurantservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "restaurants")
public class Restaurant {

    private String id;
    private String name;
    private List<Dish> dishes;

}
