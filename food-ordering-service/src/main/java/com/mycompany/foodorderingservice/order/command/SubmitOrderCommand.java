package com.mycompany.foodorderingservice.order.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitOrderCommand {

    @TargetAggregateIdentifier
    private String orderId;

}
