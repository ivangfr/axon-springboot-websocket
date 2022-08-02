package com.ivanfranchin.foodorderingservice.restaurant.repository;

import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
