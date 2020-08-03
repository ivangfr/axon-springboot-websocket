package com.mycompany.foodorderingservice.order.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderItemCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private String itemId;
    private Float itemPrice;
    private Short quantity;

}
