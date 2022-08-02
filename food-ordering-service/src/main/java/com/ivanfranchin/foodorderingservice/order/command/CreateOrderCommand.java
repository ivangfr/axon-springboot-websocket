package com.ivanfranchin.foodorderingservice.order.command;

import com.ivanfranchin.foodorderingservice.order.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private String id;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;
    private Set<OrderItem> items;
}
