package com.mycompany.restaurantservice.repository;

import com.mycompany.restaurantservice.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
