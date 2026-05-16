package com.ivanfranchin.restaurantservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "restaurant")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;
        Dish dish = (Dish) o;
        return id != null && id.equals(dish.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
