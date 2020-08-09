package com.mycompany.restaurantservice.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class RestaurantOrderDto {

    private String id;
    private String customerName;
    private String customerAddress;
    private String status;
    private BigDecimal total;
    private ZonedDateTime createdAt;

}
