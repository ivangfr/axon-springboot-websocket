package com.ivanfranchin.foodorderingservice.restaurant.model;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "restaurants")
public class Restaurant {

  @Id private String id;
  private String name;
  private Set<Dish> dishes = new LinkedHashSet<>();
}
