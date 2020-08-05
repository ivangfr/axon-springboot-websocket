package com.mycompany.foodorderingservice.order.aggregate;

import com.mycompany.foodorderingservice.order.command.AddOrderItemCommand;
import com.mycompany.foodorderingservice.order.command.DeleteOrderItemCommand;
import com.mycompany.foodorderingservice.order.command.OpenOrderCommand;
import com.mycompany.foodorderingservice.order.event.OrderItemAddedEvent;
import com.mycompany.foodorderingservice.order.event.OrderItemDeletedEvent;
import com.mycompany.foodorderingservice.order.event.OrderOpenedEvent;
import com.mycompany.foodorderingservice.order.exception.OrderItemNotFoundException;
import com.mycompany.foodorderingservice.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private OrderStatus status;

    private String customerId;
    private String customerName;
    private String customerAddress;

    private String restaurantId;
    private String restaurantName;

    private Set<OrderItem> items;

    // -- Open Order

    @CommandHandler
    public OrderAggregate(OpenOrderCommand command) {
        AggregateLifecycle.apply(new OrderOpenedEvent(command.getOrderId(),
                command.getCustomerId(), command.getCustomerName(), command.getCustomerAddress(),
                command.getRestaurantId(), command.getRestaurantName(), OrderStatus.OPENED));
    }

    @EventSourcingHandler
    public void handle(OrderOpenedEvent event) {
        this.orderId = event.getOrderId();
        this.customerId = event.getCustomerId();
        this.customerName = event.getCustomerName();
        this.customerAddress = event.getCustomerAddress();
        this.restaurantId = event.getRestaurantId();
        this.restaurantName = event.getRestaurantName();
        this.status = event.getStatus();
        this.items = new LinkedHashSet<>();
    }

    // -- Add Order Item

    @CommandHandler
    public void handle(AddOrderItemCommand command) {
        AggregateLifecycle.apply(new OrderItemAddedEvent(command.getOrderId(), command.getItemId(), command.getDishId(),
                command.getDishName(), command.getDishPrice(), command.getQuantity()));
    }

    @EventSourcingHandler
    public void handle(OrderItemAddedEvent event) {
        this.orderId = event.getOrderId();
        this.items.add(new OrderItem(event.getItemId(), event.getDishId(), event.getDishName(), event.getDishPrice(), event.getQuantity()));
    }

    // -- Delete Order Item

    @CommandHandler
    public void handle(DeleteOrderItemCommand command) {
        if (this.items.stream().noneMatch(i -> i.getId().equals(command.getItemId()))) {
            throw new OrderItemNotFoundException(command.getOrderId(), command.getItemId());
        }
        AggregateLifecycle.apply(new OrderItemDeletedEvent(command.getOrderId(), command.getItemId()));
    }

    @EventSourcingHandler
    public void handle(OrderItemDeletedEvent event) {
        this.orderId = event.getOrderId();
        this.items.removeIf(i -> i.getId().equals(event.getItemId()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

        private String id;
        private String dishId;
        private String dishName;
        private Float dishPrice;
        private Short quantity;
    }

}
