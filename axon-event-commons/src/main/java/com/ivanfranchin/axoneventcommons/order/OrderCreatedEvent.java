package com.ivanfranchin.axoneventcommons.order;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

public class OrderCreatedEvent implements OrderEvent {

    private String id;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;
    private String status;
    private BigDecimal total;
    private ZonedDateTime createdAt;
    private Set<OrderItem> items;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String id, String customerId, String customerName, String customerAddress, String restaurantId, String restaurantName, String status, BigDecimal total, ZonedDateTime createdAt, Set<OrderItem> items) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCreatedEvent that = (OrderCreatedEvent) o;
        return Objects.equals(id, that.id) && Objects.equals(customerId, that.customerId) && Objects.equals(customerName, that.customerName) && Objects.equals(customerAddress, that.customerAddress) && Objects.equals(restaurantId, that.restaurantId) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(status, that.status) && Objects.equals(total, that.total) && Objects.equals(createdAt, that.createdAt) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, customerName, customerAddress, restaurantId, restaurantName, status, total, createdAt, items);
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", status='" + status + '\'' +
                ", total=" + total +
                ", createdAt=" + createdAt +
                ", items=" + items +
                '}';
    }

    public static class OrderItem {

        private String dishId;
        private String dishName;
        private BigDecimal dishPrice;
        private Short quantity;

        public OrderItem() {
        }

        public OrderItem(String dishId, String dishName, BigDecimal dishPrice, Short quantity) {
            this.dishId = dishId;
            this.dishName = dishName;
            this.dishPrice = dishPrice;
            this.quantity = quantity;
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

        public Short getQuantity() {
            return quantity;
        }

        public void setQuantity(Short quantity) {
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderItem orderItem = (OrderItem) o;
            return Objects.equals(dishId, orderItem.dishId) && Objects.equals(dishName, orderItem.dishName) && Objects.equals(dishPrice, orderItem.dishPrice) && Objects.equals(quantity, orderItem.quantity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dishId, dishName, dishPrice, quantity);
        }

        @Override
        public String toString() {
            return "OrderItem{" +
                    "dishId='" + dishId + '\'' +
                    ", dishName='" + dishName + '\'' +
                    ", dishPrice=" + dishPrice +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
