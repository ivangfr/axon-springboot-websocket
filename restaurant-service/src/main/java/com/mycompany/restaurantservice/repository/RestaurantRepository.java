package com.mycompany.restaurantservice.repository;

import com.mycompany.restaurantservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
}
