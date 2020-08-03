package com.mycompany.foodorderingservice.restaurant.repository;

import com.mycompany.foodorderingservice.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
}
