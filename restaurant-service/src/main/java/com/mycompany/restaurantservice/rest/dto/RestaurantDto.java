package com.mycompany.restaurantservice.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RestaurantDto {

    private String id;
    private String name;
    private List<Dish> dishes;

    @Data
    public static class Dish {
        private String id;
        private String name;
        private BigDecimal price;
    }

}
