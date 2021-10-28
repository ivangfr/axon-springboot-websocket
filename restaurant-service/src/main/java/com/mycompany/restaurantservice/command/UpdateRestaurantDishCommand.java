package com.mycompany.restaurantservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRestaurantDishCommand {

    @TargetAggregateIdentifier
    private String restaurantId;
    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;
}
