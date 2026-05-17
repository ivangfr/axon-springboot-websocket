package com.ivanfranchin.foodorderingservice.order.rest;

import com.ivanfranchin.foodorderingservice.order.model.Order;
import com.ivanfranchin.foodorderingservice.order.model.OrderItem;
import com.ivanfranchin.foodorderingservice.order.model.OrderStatus;
import com.ivanfranchin.foodorderingservice.order.query.GetOrderQuery;
import com.ivanfranchin.foodorderingservice.order.query.GetOrdersQuery;
import com.ivanfranchin.foodorderingservice.order.service.OrderService;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueryGateway queryGateway;

    @MockitoBean
    private OrderService orderService;

    @Test
    void testGetOrders() throws Exception {
        Order order = new Order();
        order.setId("o1");
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(new BigDecimal("25.50"));
        order.setCreatedAt(ZonedDateTime.now());
        order.setCustomerId("c1");
        order.setRestaurantName("Pizza Hut");
        order.setItems(Set.of(new OrderItem("d1", "Pizza", new BigDecimal("12.75"), (short) 2)));

        doReturn(CompletableFuture.completedFuture(List.of(order)))
                .when(queryGateway).query(any(GetOrdersQuery.class), any(ResponseType.class));

        MvcResult mvcResult = mockMvc.perform(get("/api/orders"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("o1"))
                .andExpect(jsonPath("$[0].restaurantName").value("Pizza Hut"))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }

    @Test
    void testGetOrder() throws Exception {
        Order order = new Order();
        order.setId("o1");
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(new BigDecimal("25.50"));
        order.setCreatedAt(ZonedDateTime.now());
        order.setItems(Set.of(new OrderItem("d1", "Pizza", new BigDecimal("12.75"), (short) 2)));

        doReturn(CompletableFuture.completedFuture(order))
                .when(queryGateway).query(any(GetOrderQuery.class), any(Class.class));

        MvcResult mvcResult = mockMvc.perform(get("/api/orders/550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("o1"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void testCreateOrder() throws Exception {
        String orderId = UUID.randomUUID().toString();
        doReturn(CompletableFuture.completedFuture(orderId))
                .when(orderService).createOrder(any());

        MvcResult mvcResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerId": "%s",
                                    "restaurantId": "%s",
                                    "items": [
                                        {"dishId": "%s", "quantity": 2}
                                    ]
                                }
                                """.formatted(
                                        UUID.randomUUID(),
                                        UUID.randomUUID(),
                                        UUID.randomUUID())))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(orderId));
    }

    @Test
    void testCreateOrder_whenInvalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
