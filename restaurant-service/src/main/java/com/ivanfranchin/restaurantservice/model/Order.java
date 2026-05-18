package com.ivanfranchin.restaurantservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "restaurant")
@Entity
@Table(name = "orders")
public class Order {

  @Id private String id;
  private String customerName;
  private String customerAddress;
  private String status;
  private BigDecimal total = BigDecimal.ZERO;
  private ZonedDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @JdbcTypeCode(SqlTypes.JSON)
  private Set<OrderItem> items;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Order)) return false;
    Order order = (Order) o;
    return id != null && id.equals(order.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
