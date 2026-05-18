package com.ivanfranchin.foodorderingservice.restaurant.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Dish {

  private String id;
  private String name;
  private BigDecimal price;
}
