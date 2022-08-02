package com.ivanfranchin.customerservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerCommand {

    @TargetAggregateIdentifier
    private String id;
    private String name;
    private String address;
}
