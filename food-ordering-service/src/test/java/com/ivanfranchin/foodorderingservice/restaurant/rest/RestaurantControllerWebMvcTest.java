package com.ivanfranchin.foodorderingservice.restaurant.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ivanfranchin.foodorderingservice.restaurant.model.Dish;
import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import com.ivanfranchin.foodorderingservice.restaurant.service.RestaurantService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerWebMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RestaurantService restaurantService;

  @Test
  void testGetRestaurants() throws Exception {
    Dish dish = new Dish();
    dish.setId("d1");
    dish.setName("Pizza Margherita");
    dish.setPrice(new BigDecimal("6.99"));

    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");
    restaurant.setDishes(Set.of(dish));

    when(restaurantService.getRestaurants()).thenReturn(List.of(restaurant));

    mockMvc
        .perform(get("/api/restaurants"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("r1"))
        .andExpect(jsonPath("$[0].name").value("Pizza Hut"))
        .andExpect(jsonPath("$[0].dishes[0].id").value("d1"));
  }
}
