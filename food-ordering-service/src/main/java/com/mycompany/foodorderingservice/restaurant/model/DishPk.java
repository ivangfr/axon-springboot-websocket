package com.mycompany.foodorderingservice.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishPk implements Serializable {

    private String id;
    private String restaurant;

}
