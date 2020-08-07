package com.mycompany.foodorderingservice.order.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderItemCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private String itemId;
    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;
    private Short quantity;

}
