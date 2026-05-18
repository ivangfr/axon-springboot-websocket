package com.ivanfranchin.axoneventcommons.customer;

import java.util.Objects;

public class CustomerAddedEvent implements CustomerEvent {

  private String id;
  private String name;
  private String address;

  public CustomerAddedEvent() {}

  public CustomerAddedEvent(String id, String name, String address) {
    this.id = id;
    this.name = name;
    this.address = address;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomerAddedEvent that = (CustomerAddedEvent) o;
    return Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(address, that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, address);
  }

  @Override
  public String toString() {
    return "CustomerAddedEvent{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", address='"
        + address
        + '\''
        + '}';
  }
}
