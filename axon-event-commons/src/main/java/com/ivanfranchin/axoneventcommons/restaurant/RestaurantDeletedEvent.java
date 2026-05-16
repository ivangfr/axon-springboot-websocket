package com.ivanfranchin.axoneventcommons.restaurant;

import java.util.Objects;

public class RestaurantDeletedEvent implements RestaurantEvent {

    private String id;

    public RestaurantDeletedEvent() {
    }

    public RestaurantDeletedEvent(String id) {
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
        RestaurantDeletedEvent that = (RestaurantDeletedEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RestaurantDeletedEvent{" +
                "id='" + id + '\'' +
                '}';
    }
}
