package com.ivanfranchin.customerservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCustomerCommand {

    @TargetAggregateIdentifier
    private String id;
}
