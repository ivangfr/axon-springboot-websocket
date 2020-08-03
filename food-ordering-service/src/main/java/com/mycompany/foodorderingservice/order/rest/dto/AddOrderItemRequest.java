package com.mycompany.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class AddOrderItemRequest {

    @Schema(example = "xyz")
    @NotBlank
    private String dishId;

    @Schema(example = "1")
    @Positive
    private Short quantity;

}
