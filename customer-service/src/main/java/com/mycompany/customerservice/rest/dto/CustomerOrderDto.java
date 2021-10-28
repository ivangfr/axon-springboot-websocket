package com.mycompany.customerservice.rest.dto;

import com.mycompany.customerservice.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class CustomerOrderDto {

    private String id;
    private String restaurantName;
    private String status;
    private BigDecimal total;
    private ZonedDateTime createdAt;
    private Set<OrderItem> items;
}
