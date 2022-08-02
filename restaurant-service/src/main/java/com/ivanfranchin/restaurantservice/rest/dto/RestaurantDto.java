package com.ivanfranchin.restaurantservice.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class RestaurantDto {

    private String id;
    private String name;
    private Set<Dish> dishes;

    @Data
    public static class Dish {
        private String id;
        private String name;
        private BigDecimal price;
    }
}
