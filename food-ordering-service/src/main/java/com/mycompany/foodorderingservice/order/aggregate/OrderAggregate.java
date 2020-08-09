package com.mycompany.foodorderingservice.order.aggregate;

import com.mycompany.axoneventcommons.order.OrderCreatedEvent;
import com.mycompany.foodorderingservice.order.command.CreateOrderCommand;
import com.mycompany.foodorderingservice.order.model.OrderItem;
import com.mycompany.foodorderingservice.order.model.OrderStatus;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private OrderStatus status;
    private BigDecimal total;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;
    private Set<OrderItem> items;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        Set<OrderCreatedEvent.OrderItem> evtItems = new LinkedHashSet<>();
        BigDecimal bdTotal = BigDecimal.ZERO;
        for (OrderItem orderItem : command.getItems()) {
            evtItems.add(new OrderCreatedEvent.OrderItem(orderItem.getDishId(), orderItem.getDishName(),
                    orderItem.getDishPrice(), orderItem.getQuantity()));
            bdTotal = bdTotal.add(orderItem.getDishPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }

        AggregateLifecycle.apply(new OrderCreatedEvent(command.getOrderId(), command.getCustomerId(),
                command.getCustomerName(), command.getCustomerAddress(), command.getRestaurantId(),
                command.getRestaurantName(), OrderStatus.CREATED.name(), bdTotal, evtItems));
    }

    @EventSourcingHandler
    public void handle(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.status = OrderStatus.valueOf(event.getStatus());
        this.total = event.getTotal();
        this.customerId = event.getCustomerId();
        this.customerName = event.getCustomerName();
        this.customerAddress = event.getCustomerAddress();
        this.restaurantId = event.getRestaurantId();
        this.restaurantName = event.getRestaurantName();
        this.items = event.getItems().stream()
                .map(i -> new OrderItem(i.getDishId(), i.getDishName(), i.getDishPrice(), i.getQuantity()))
                .collect(Collectors.toSet());
    }

}
