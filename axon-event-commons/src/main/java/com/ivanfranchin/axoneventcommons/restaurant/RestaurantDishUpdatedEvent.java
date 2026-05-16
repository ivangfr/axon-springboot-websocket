package com.ivanfranchin.axoneventcommons.restaurant;

import java.math.BigDecimal;
import java.util.Objects;

public class RestaurantDishUpdatedEvent implements RestaurantEvent {

    private String restaurantId;
    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;

    public RestaurantDishUpdatedEvent() {
    }

    public RestaurantDishUpdatedEvent(String restaurantId, String dishId, String dishName, BigDecimal dishPrice) {
        this.restaurantId = restaurantId;
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public BigDecimal getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(BigDecimal dishPrice) {
        this.dishPrice = dishPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDishUpdatedEvent that = (RestaurantDishUpdatedEvent) o;
        return Objects.equals(restaurantId, that.restaurantId) && Objects.equals(dishId, that.dishId) && Objects.equals(dishName, that.dishName) && Objects.equals(dishPrice, that.dishPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, dishId, dishName, dishPrice);
    }

    @Override
    public String toString() {
        return "RestaurantDishUpdatedEvent{" +
                "restaurantId='" + restaurantId + '\'' +
                ", dishId='" + dishId + '\'' +
                ", dishName='" + dishName + '\'' +
                ", dishPrice=" + dishPrice +
                '}';
    }
}
