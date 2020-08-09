package com.mycompany.restaurantservice.rest.dto;

import com.mycompany.restaurantservice.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class RestaurantOrderDto {

    private String id;
    private String customerName;
    private String customerAddress;
    private String status;
    private BigDecimal total;
    private ZonedDateTime createdAt;
    private Set<OrderItem> items;

}
