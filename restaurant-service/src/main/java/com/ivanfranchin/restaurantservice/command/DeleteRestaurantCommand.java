package com.ivanfranchin.restaurantservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRestaurantCommand {

    @TargetAggregateIdentifier
    private String id;
}
