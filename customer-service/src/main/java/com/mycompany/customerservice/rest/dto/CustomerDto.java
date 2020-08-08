package com.mycompany.customerservice.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerDto {

    private String id;
    private String name;
    private String address;
    private List<Order> orders;

    @Data
    public static class Order {
        private String id;
        private String restaurantName;
        private String status;
        private BigDecimal total;
    }

}
