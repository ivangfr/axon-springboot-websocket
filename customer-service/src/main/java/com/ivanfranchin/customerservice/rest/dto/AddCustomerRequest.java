package com.ivanfranchin.customerservice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCustomerRequest {

    @Schema(example = "Ivan Franchin")
    @NotBlank
    private String name;

    @Schema(example = "Brooklyn 12, NYC")
    @NotBlank
    private String address;
}
