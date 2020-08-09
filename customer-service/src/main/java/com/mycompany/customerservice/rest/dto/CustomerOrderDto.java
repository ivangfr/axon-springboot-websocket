package com.mycompany.customerservice.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class CustomerOrderDto {

    private String id;
    private String restaurantName;
    private String status;
    private BigDecimal total;
    private ZonedDateTime createdAt;

}
