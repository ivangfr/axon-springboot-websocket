package com.mycompany.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
public class AddOrderItemRequest {

    private UUID dishId;

    @Schema(example = "1")
    @Positive
    private Short quantity;

}
