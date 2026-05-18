package com.ivanfranchin.foodorderingservice.order.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivanfranchin.axoneventcommons.order.OrderCreatedEvent;
import com.ivanfranchin.foodorderingservice.customer.rest.dto.CustomerResponse;
import com.ivanfranchin.foodorderingservice.restaurant.rest.dto.RestaurantResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class FoodOrderingDtoJsonTest {

  @Autowired private JacksonTester<CreateOrderRequest> createOrderRequestJson;

  @Autowired private JacksonTester<OrderResponse> orderResponseJson;

  @Autowired private JacksonTester<CustomerResponse> customerResponseJson;

  @Autowired private JacksonTester<RestaurantResponse> restaurantResponseJson;

  @Autowired private JacksonTester<OrderCreatedEvent> orderCreatedEventJson;

  @Test
  void testCreateOrderRequestSerialization() throws Exception {
    CreateOrderRequest.OrderItemRequest item =
        new CreateOrderRequest.OrderItemRequest(java.util.UUID.randomUUID(), (short) 2);
    CreateOrderRequest request =
        new CreateOrderRequest(
            java.util.UUID.randomUUID(), java.util.UUID.randomUUID(), Set.of(item));
    JsonContent<CreateOrderRequest> json = createOrderRequestJson.write(request);
    assertThat(json).extractingJsonPathStringValue("$.customerId").isNotNull();
    assertThat(json).extractingJsonPathStringValue("$.restaurantId").isNotNull();
    assertThat(json).extractingJsonPathArrayValue("$.items").hasSize(1);
  }

  @Test
  void testCreateOrderRequestDeserialization() throws Exception {
    String content =
        """
                {
                    "customerId": "550e8400-e29b-41d4-a716-446655440000",
                    "restaurantId": "550e8400-e29b-41d4-a716-446655440001",
                    "items": [{"dishId": "550e8400-e29b-41d4-a716-446655440002", "quantity": 2}]
                }
                """;
    CreateOrderRequest request = createOrderRequestJson.parseObject(content);
    assertThat(request.customerId()).isNotNull();
    assertThat(request.restaurantId()).isNotNull();
    assertThat(request.items()).hasSize(1);
  }

  @Test
  void testOrderResponseSerialization() throws Exception {
    OrderResponse.OrderItem item =
        new OrderResponse.OrderItem("d1", "Pizza Margherita", new BigDecimal("6.99"), (short) 2);
    OrderResponse response =
        new OrderResponse(
            "o1",
            "c1",
            "John",
            "123 Main St",
            "r1",
            "Pizza Hut",
            com.ivanfranchin.foodorderingservice.order.model.OrderStatus.CREATED,
            new BigDecimal("13.98"),
            ZonedDateTime.parse("2025-01-15T10:30:00Z"),
            Set.of(item));
    JsonContent<OrderResponse> json = orderResponseJson.write(response);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("o1");
    assertThat(json).extractingJsonPathStringValue("$.restaurantName").isEqualTo("Pizza Hut");
    assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo("CREATED");
    assertThat(json)
        .extractingJsonPathStringValue("$.items[0].dishName")
        .isEqualTo("Pizza Margherita");
  }

  @Test
  void testCustomerResponseSerialization() throws Exception {
    CustomerResponse response = new CustomerResponse("c1", "John", "123 Main St");
    JsonContent<CustomerResponse> json = customerResponseJson.write(response);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("c1");
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John");
  }

  @Test
  void testRestaurantResponseSerialization() throws Exception {
    RestaurantResponse.Dish dish =
        new RestaurantResponse.Dish("d1", "Pizza Margherita", new BigDecimal("6.99"));
    RestaurantResponse response = new RestaurantResponse("r1", "Pizza Hut", Set.of(dish));
    JsonContent<RestaurantResponse> json = restaurantResponseJson.write(response);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("r1");
    assertThat(json).extractingJsonPathStringValue("$.dishes[0].id").isEqualTo("d1");
  }

  @Test
  void testOrderCreatedEventSerialization() throws Exception {
    OrderCreatedEvent.OrderItem item =
        new OrderCreatedEvent.OrderItem(
            "d1", "Pizza Margherita", new BigDecimal("6.99"), (short) 2);
    OrderCreatedEvent event =
        new OrderCreatedEvent(
            "o1",
            "c1",
            "John",
            "123 Main St",
            "r1",
            "Pizza Hut",
            "CREATED",
            new BigDecimal("13.98"),
            ZonedDateTime.parse("2025-01-15T10:30:00Z"),
            Set.of(item));
    JsonContent<OrderCreatedEvent> json = orderCreatedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("o1");
    assertThat(json).extractingJsonPathStringValue("$.customerId").isEqualTo("c1");
    assertThat(json).extractingJsonPathStringValue("$.restaurantName").isEqualTo("Pizza Hut");
    assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo("CREATED");
    assertThat(json).extractingJsonPathStringValue("$.items[0].dishId").isEqualTo("d1");
  }

  @Test
  void testOrderCreatedEventDeserialization() throws Exception {
    String content =
        """
                {
                    "id": "o1",
                    "customerId": "c1",
                    "customerName": "John",
                    "customerAddress": "123 Main St",
                    "restaurantId": "r1",
                    "restaurantName": "Pizza Hut",
                    "status": "CREATED",
                    "total": 13.98,
                    "createdAt": "2025-01-15T10:30:00Z",
                    "items": [
                        {"dishId": "d1", "dishName": "Pizza Margherita", "dishPrice": 6.99, "quantity": 2}
                    ]
                }
                """;
    OrderCreatedEvent event = orderCreatedEventJson.parseObject(content);
    assertThat(event.getId()).isEqualTo("o1");
    assertThat(event.getCustomerId()).isEqualTo("c1");
    assertThat(event.getStatus()).isEqualTo("CREATED");
    assertThat(event.getItems()).hasSize(1);
    assertThat(event.getItems().iterator().next().getDishName()).isEqualTo("Pizza Margherita");
  }
}
