package com.ivanfranchin.customerservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {

  private String dishName;
  private BigDecimal dishPrice;
  private Short quantity;
}
