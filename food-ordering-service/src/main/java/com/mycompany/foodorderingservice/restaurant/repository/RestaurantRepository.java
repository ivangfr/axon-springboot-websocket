package com.mycompany.foodorderingservice.restaurant.repository;

import com.mycompany.foodorderingservice.restaurant.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
