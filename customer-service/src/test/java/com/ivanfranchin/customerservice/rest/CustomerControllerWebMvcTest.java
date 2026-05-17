package com.ivanfranchin.customerservice.rest;

import com.ivanfranchin.customerservice.command.AddCustomerCommand;
import com.ivanfranchin.customerservice.command.UpdateCustomerCommand;
import com.ivanfranchin.customerservice.model.Customer;
import com.ivanfranchin.customerservice.model.Order;
import com.ivanfranchin.customerservice.model.OrderItem;
import com.ivanfranchin.customerservice.query.GetCustomerOrdersQuery;
import com.ivanfranchin.customerservice.query.GetCustomerQuery;
import com.ivanfranchin.customerservice.query.GetCustomersQuery;
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

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommandGateway commandGateway;

    @MockitoBean
    private QueryGateway queryGateway;

    @Test
    void testGetCustomers() throws Exception {
        Customer customer = new Customer();
        customer.setId("c1");
        customer.setName("John");
        customer.setAddress("123 Main St");

        doReturn(CompletableFuture.completedFuture(List.of(customer)))
                .when(queryGateway).query(any(GetCustomersQuery.class), any(ResponseType.class));

        MvcResult mvcResult = mockMvc.perform(get("/api/customers"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("c1"))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].address").value("123 Main St"));
    }

    @Test
    void testGetCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setId("c1");
        customer.setName("John");
        customer.setAddress("123 Main St");

        doReturn(CompletableFuture.completedFuture(customer))
                .when(queryGateway).query(any(GetCustomerQuery.class), any(Class.class));

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/c1"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    void testAddCustomer() throws Exception {
        doReturn(CompletableFuture.completedFuture("c1"))
                .when(commandGateway).send(any(AddCustomerCommand.class));

        MvcResult mvcResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane\",\"address\":\"456 Oak Ave\"}"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(content().string("c1"));
    }

    @Test
    void testAddCustomer_whenInvalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"address\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        doReturn(CompletableFuture.completedFuture("c1"))
                .when(commandGateway).send(any(UpdateCustomerCommand.class));

        MvcResult mvcResult = mockMvc.perform(patch("/api/customers/c1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Updated\",\"address\":\"789 Pine Rd\"}"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string("c1"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/c1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCustomerOrders() throws Exception {
        Order order = new Order();
        order.setId("o1");
        order.setRestaurantName("Pizza Place");
        order.setStatus("CREATED");
        order.setTotal(new BigDecimal("25.50"));
        order.setCreatedAt(ZonedDateTime.now());

        OrderItem item = new OrderItem();
        item.setDishName("Pizza");
        item.setDishPrice(new BigDecimal("12.75"));
        item.setQuantity((short) 2);
        order.setItems(Set.of(item));

        doReturn(CompletableFuture.completedFuture(List.of(order)))
                .when(queryGateway).query(any(GetCustomerOrdersQuery.class), any(ResponseType.class));

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/c1/orders"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("o1"))
                .andExpect(jsonPath("$[0].restaurantName").value("Pizza Place"))
                .andExpect(jsonPath("$[0].items[0].dishName").value("Pizza"));
    }
}
