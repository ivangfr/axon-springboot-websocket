package com.ivanfranchin.restaurantservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"dishes", "orders"})
@Entity
@Table(name = "restaurants")
public class Restaurant {

  @Id private String id;
  private String name;

  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "restaurant",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<Dish> dishes = new LinkedHashSet<>();

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Order> orders = new LinkedHashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Restaurant)) return false;
    Restaurant that = (Restaurant) o;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
