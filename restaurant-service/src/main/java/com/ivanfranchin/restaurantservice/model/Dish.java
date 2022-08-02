package com.ivanfranchin.restaurantservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@ToString(exclude = "restaurant")
@EqualsAndHashCode(exclude = "restaurant")
@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    private String id;
    private String name;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
