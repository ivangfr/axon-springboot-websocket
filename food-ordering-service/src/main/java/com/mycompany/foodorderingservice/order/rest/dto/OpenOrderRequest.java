package com.mycompany.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OpenOrderRequest {

    @Schema(example = "abc")
    @NotBlank
    private String customerId;

    @Schema(example = "def")
    @NotBlank
    private String restaurantId;

}
