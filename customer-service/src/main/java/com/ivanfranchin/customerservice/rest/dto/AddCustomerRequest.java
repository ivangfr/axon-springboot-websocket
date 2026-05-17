package com.ivanfranchin.customerservice.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AddCustomerRequest(
        @NotBlank String name,
        @NotBlank String address) {
}
