package com.mycompany.foodorderingservice.order.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class OpenOrderRequest {

    @Schema(example = "...")
    @NotBlank
    private UUID customerId;

    @Schema(example = "...")
    @NotBlank
    private UUID restaurantId;

}
