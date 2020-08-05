package com.mycompany.foodorderingservice.restaurant.repository;

import com.mycompany.foodorderingservice.restaurant.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, String> {
}
