package com.ivanfranchin.customerservice.rest.dto;

import com.ivanfranchin.customerservice.model.OrderItem;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public record CustomerOrderResponse(String id, String restaurantName, String status, BigDecimal total,
                                    ZonedDateTime createdAt, Set<OrderItem> items) {
}
