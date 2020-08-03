package com.mycompany.foodorderingservice.restaurant.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@ToString(exclude = "dishes")
@EqualsAndHashCode(exclude = "dishes")
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    private String id;
    private String name;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dish> dishes;

    public Restaurant(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
