package com.ivanfranchin.customerservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateCustomerRequest {

    @Schema(example = "Ivan Franchin")
    private String name;

    @Schema(example = "Bronx 182, NYC")
    private String address;
}
