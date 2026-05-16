package com.ivanfranchin.axoneventcommons.restaurant;

import java.util.Objects;

public class RestaurantDishDeletedEvent implements RestaurantEvent {

    private String restaurantId;
    private String dishId;

    public RestaurantDishDeletedEvent() {
    }

    public RestaurantDishDeletedEvent(String restaurantId, String dishId) {
        this.restaurantId = restaurantId;
        this.dishId = dishId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDishDeletedEvent that = (RestaurantDishDeletedEvent) o;
        return Objects.equals(restaurantId, that.restaurantId) && Objects.equals(dishId, that.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, dishId);
    }

    @Override
    public String toString() {
        return "RestaurantDishDeletedEvent{" +
                "restaurantId='" + restaurantId + '\'' +
                ", dishId='" + dishId + '\'' +
                '}';
    }
}
