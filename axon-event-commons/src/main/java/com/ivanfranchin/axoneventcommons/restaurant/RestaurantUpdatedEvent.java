package com.ivanfranchin.axoneventcommons.restaurant;

import java.util.Objects;

public class RestaurantUpdatedEvent implements RestaurantEvent {

  private String id;
  private String name;

  public RestaurantUpdatedEvent() {}

  public RestaurantUpdatedEvent(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RestaurantUpdatedEvent that = (RestaurantUpdatedEvent) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "RestaurantUpdatedEvent{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
  }
}
