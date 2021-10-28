package com.mycompany.foodorderingservice.restaurant.repository;

import com.mycompany.foodorderingservice.restaurant.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
