package com.ivanfranchin.axoneventcommons.customer;

import java.util.Objects;

public class CustomerDeletedEvent implements CustomerEvent {

  private String id;

  public CustomerDeletedEvent() {}

  public CustomerDeletedEvent(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomerDeletedEvent that = (CustomerDeletedEvent) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "CustomerDeletedEvent{" + "id='" + id + '\'' + '}';
  }
}
