package com.ivanfranchin.customerservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddCustomerRequest(
        @Schema(example = "Ivan Franchin") @NotBlank String name,
        @Schema(example = "Brooklyn 12, NYC") @NotBlank String address) {
}
