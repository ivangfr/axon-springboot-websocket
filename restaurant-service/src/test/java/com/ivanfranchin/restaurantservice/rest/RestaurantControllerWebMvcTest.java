package com.ivanfranchin.restaurantservice.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ivanfranchin.restaurantservice.command.AddRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.AddRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.command.UpdateRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.UpdateRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.model.Dish;
import com.ivanfranchin.restaurantservice.model.Order;
import com.ivanfranchin.restaurantservice.model.OrderItem;
import com.ivanfranchin.restaurantservice.model.Restaurant;
import com.ivanfranchin.restaurantservice.query.GetRestaurantDishQuery;
import com.ivanfranchin.restaurantservice.query.GetRestaurantOrdersQuery;
import com.ivanfranchin.restaurantservice.query.GetRestaurantQuery;
import com.ivanfranchin.restaurantservice.query.GetRestaurantsQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerWebMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CommandGateway commandGateway;

  @MockitoBean private QueryGateway queryGateway;

  @Test
  void testGetRestaurants() throws Exception {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");

    doReturn(CompletableFuture.completedFuture(List.of(restaurant)))
        .when(queryGateway)
        .query(any(GetRestaurantsQuery.class), any(ResponseType.class));

    MvcResult mvcResult =
        mockMvc.perform(get("/api/restaurants")).andExpect(request().asyncStarted()).andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("r1"))
        .andExpect(jsonPath("$[0].name").value("Pizza Hut"));
  }

  @Test
  void testGetRestaurant() throws Exception {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");

    doReturn(CompletableFuture.completedFuture(restaurant))
        .when(queryGateway)
        .query(any(GetRestaurantQuery.class), any(Class.class));

    MvcResult mvcResult =
        mockMvc
            .perform(get("/api/restaurants/550e8400-e29b-41d4-a716-446655440000"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("r1"))
        .andExpect(jsonPath("$.name").value("Pizza Hut"));
  }

  @Test
  void testAddRestaurant() throws Exception {
    doReturn(CompletableFuture.completedFuture("r1"))
        .when(commandGateway)
        .send(any(AddRestaurantCommand.class));

    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/api/restaurants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Pizza Hut\"}"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").value("r1"));
  }

  @Test
  void testAddRestaurant_whenInvalidRequest_returnsBadRequest() throws Exception {
    mockMvc
        .perform(
            post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateRestaurant() throws Exception {
    doReturn(CompletableFuture.completedFuture("r1"))
        .when(commandGateway)
        .send(any(UpdateRestaurantCommand.class));

    MvcResult mvcResult =
        mockMvc
            .perform(
                patch("/api/restaurants/550e8400-e29b-41d4-a716-446655440000")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Pizza Hut Updated\"}"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("r1"));
  }

  @Test
  void testDeleteRestaurant() throws Exception {
    mockMvc
        .perform(delete("/api/restaurants/550e8400-e29b-41d4-a716-446655440000"))
        .andExpect(status().isNoContent());
  }

  @Test
  void testAddRestaurantDish() throws Exception {
    doReturn(CompletableFuture.completedFuture("d1"))
        .when(commandGateway)
        .send(any(AddRestaurantDishCommand.class));

    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/api/restaurants/550e8400-e29b-41d4-a716-446655440000/dishes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Pizza Margherita\",\"price\":6.99}"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isString());
  }

  @Test
  void testAddRestaurantDish_whenInvalidRequest_returnsBadRequest() throws Exception {
    mockMvc
        .perform(
            post("/api/restaurants/550e8400-e29b-41d4-a716-446655440000/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"price\":-1}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetRestaurantDish() throws Exception {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");

    Dish dish = new Dish();
    dish.setId("d1");
    dish.setName("Pizza Margherita");
    dish.setPrice(new BigDecimal("6.99"));
    dish.setRestaurant(restaurant);

    doReturn(CompletableFuture.completedFuture(dish))
        .when(queryGateway)
        .query(any(GetRestaurantDishQuery.class), any(Class.class));

    MvcResult mvcResult =
        mockMvc
            .perform(
                get(
                    "/api/restaurants/550e8400-e29b-41d4-a716-446655440000/dishes/550e8400-e29b-41d4-a716-446655440001"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.restaurantId").value("r1"))
        .andExpect(jsonPath("$.dishId").value("d1"))
        .andExpect(jsonPath("$.dishName").value("Pizza Margherita"));
  }

  @Test
  void testUpdateRestaurantDish() throws Exception {
    doReturn(CompletableFuture.completedFuture("d1"))
        .when(commandGateway)
        .send(any(UpdateRestaurantDishCommand.class));

    MvcResult mvcResult =
        mockMvc
            .perform(
                patch(
                        "/api/restaurants/550e8400-e29b-41d4-a716-446655440000/dishes/550e8400-e29b-41d4-a716-446655440001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Pizza Margherita 35cm\",\"price\":7.99}"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("d1"));
  }

  @Test
  void testDeleteRestaurantDish() throws Exception {
    mockMvc
        .perform(
            delete(
                "/api/restaurants/550e8400-e29b-41d4-a716-446655440000/dishes/550e8400-e29b-41d4-a716-446655440001"))
        .andExpect(status().isNoContent());
  }

  @Test
  void testGetRestaurantOrders() throws Exception {
    Order order = new Order();
    order.setId("o1");
    order.setCustomerName("John");
    order.setStatus("CREATED");
    order.setTotal(new BigDecimal("25.50"));
    order.setItems(Set.of(new OrderItem("d1", "Pizza", new BigDecimal("12.75"), (short) 2)));

    doReturn(CompletableFuture.completedFuture(List.of(order)))
        .when(queryGateway)
        .query(any(GetRestaurantOrdersQuery.class), any(ResponseType.class));

    MvcResult mvcResult =
        mockMvc
            .perform(get("/api/restaurants/550e8400-e29b-41d4-a716-446655440000/orders"))
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc
        .perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("o1"))
        .andExpect(jsonPath("$[0].customerName").value("John"));
  }
}
