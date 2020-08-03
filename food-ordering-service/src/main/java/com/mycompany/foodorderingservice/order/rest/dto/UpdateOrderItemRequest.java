package com.mycompany.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateOrderItemRequest {

    @Schema(example = "2")
    private Short quantity;

}
