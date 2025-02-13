package com.ivanfranchin.customerservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCustomerRequest(
        @Schema(example = "Ivan Franchin") String name,
        @Schema(example = "Bronx 182, NYC") String address) {
}
