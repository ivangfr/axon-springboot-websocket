package com.ivanfranchin.foodorderingservice.order.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

  private String dishId;
  private String dishName;
  private BigDecimal dishPrice;
  private Short quantity;
}
