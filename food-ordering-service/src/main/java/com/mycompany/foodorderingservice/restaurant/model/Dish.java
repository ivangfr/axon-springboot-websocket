package com.mycompany.foodorderingservice.restaurant.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@ToString(exclude = "restaurant")
@EqualsAndHashCode(exclude = "restaurant")
@Entity
@Table(name = "dishes")
@IdClass(DishPk.class)
public class Dish {

    @Id
    private String id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String name;
    private Float price;

}
