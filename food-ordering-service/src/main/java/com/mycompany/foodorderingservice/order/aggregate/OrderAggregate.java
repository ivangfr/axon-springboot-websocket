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
        AggregateLifecycle.apply(new OrderOpenedEvent(command.getOrderId(), command.getCustomerId(), command.getRestaurantId(), OrderStatus.OPENED));
    }

    @EventSourcingHandler
    public void on(OrderOpenedEvent event) {
        this.orderId = event.getOrderId();
        this.customerId = event.getCustomerId();
        this.restaurantId = event.getRestaurantId();
        this.status = event.getStatus();
        this.items = new LinkedHashSet<>();
    }

    // -- Add Order Item

    @CommandHandler
    public void handle(AddOrderItemCommand command) {
        AggregateLifecycle.apply(new OrderItemAddedEvent(command.getOrderId(), command.getItemId(), command.getItemPrice(), command.getQuantity()));
    }

    @EventSourcingHandler
    public void on(OrderItemAddedEvent event) {
        this.orderId = event.getOrderId();
        this.items.add(new OrderItem(event.getItemId(), event.getItemPrice(), event.getQuantity()));
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
    public void on(OrderItemDeletedEvent event) {
        this.orderId = event.getOrderId();
        this.items.removeIf(i -> i.getId().equals(event.getItemId()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

        private String id;
        private Float price;
        private Short quantity;
    }

}
