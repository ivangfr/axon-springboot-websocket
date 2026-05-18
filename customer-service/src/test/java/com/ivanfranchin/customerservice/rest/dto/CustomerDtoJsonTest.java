package com.ivanfranchin.customerservice.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivanfranchin.axoneventcommons.customer.CustomerAddedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerDeletedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerUpdatedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantAddedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDeletedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishAddedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishDeletedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishUpdatedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantUpdatedEvent;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class CustomerDtoJsonTest {

  @Autowired private JacksonTester<AddCustomerRequest> addCustomerRequestJson;

  @Autowired private JacksonTester<UpdateCustomerRequest> updateCustomerRequestJson;

  @Autowired private JacksonTester<CustomerResponse> customerResponseJson;

  @Autowired private JacksonTester<CustomerOrderResponse> customerOrderResponseJson;

  @Autowired private JacksonTester<CustomerAddedEvent> customerAddedEventJson;

  @Autowired private JacksonTester<CustomerUpdatedEvent> customerUpdatedEventJson;

  @Autowired private JacksonTester<CustomerDeletedEvent> customerDeletedEventJson;

  @Autowired private JacksonTester<RestaurantAddedEvent> restaurantAddedEventJson;

  @Autowired private JacksonTester<RestaurantUpdatedEvent> restaurantUpdatedEventJson;

  @Autowired private JacksonTester<RestaurantDeletedEvent> restaurantDeletedEventJson;

  @Autowired private JacksonTester<RestaurantDishAddedEvent> restaurantDishAddedEventJson;

  @Autowired private JacksonTester<RestaurantDishUpdatedEvent> restaurantDishUpdatedEventJson;

  @Autowired private JacksonTester<RestaurantDishDeletedEvent> restaurantDishDeletedEventJson;

  @Test
  void testAddCustomerRequestSerialization() throws Exception {
    AddCustomerRequest request = new AddCustomerRequest("John", "123 Main St");
    JsonContent<AddCustomerRequest> json = addCustomerRequestJson.write(request);
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John");
    assertThat(json).extractingJsonPathStringValue("$.address").isEqualTo("123 Main St");
  }

  @Test
  void testAddCustomerRequestDeserialization() throws Exception {
    String content = "{\"name\":\"Jane\",\"address\":\"456 Oak Ave\"}";
    AddCustomerRequest request = addCustomerRequestJson.parseObject(content);
    assertThat(request.name()).isEqualTo("Jane");
    assertThat(request.address()).isEqualTo("456 Oak Ave");
  }

  @Test
  void testUpdateCustomerRequestSerialization() throws Exception {
    UpdateCustomerRequest request = new UpdateCustomerRequest("John Updated", "789 Pine Rd");
    JsonContent<UpdateCustomerRequest> json = updateCustomerRequestJson.write(request);
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John Updated");
    assertThat(json).extractingJsonPathStringValue("$.address").isEqualTo("789 Pine Rd");
  }

  @Test
  void testCustomerResponseSerialization() throws Exception {
    CustomerResponse response = new CustomerResponse("c1", "John", "123 Main St");
    JsonContent<CustomerResponse> json = customerResponseJson.write(response);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("c1");
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John");
  }

  @Test
  void testCustomerOrderResponseSerialization() throws Exception {
    CustomerOrderResponse.OrderItem item =
        new CustomerOrderResponse.OrderItem("Pizza", new BigDecimal("12.75"), (short) 2);
    CustomerOrderResponse response =
        new CustomerOrderResponse(
            "o1",
            "Pizza Place",
            "CREATED",
            new BigDecimal("25.50"),
            ZonedDateTime.parse("2025-01-15T10:30:00Z"),
            Set.of(item));
    JsonContent<CustomerOrderResponse> json = customerOrderResponseJson.write(response);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("o1");
    assertThat(json).extractingJsonPathStringValue("$.restaurantName").isEqualTo("Pizza Place");
    assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo("CREATED");
  }

  @Test
  void testCustomerAddedEventSerialization() throws Exception {
    CustomerAddedEvent event = new CustomerAddedEvent("c1", "John", "123 Main St");
    JsonContent<CustomerAddedEvent> json = customerAddedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("c1");
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John");
    assertThat(json).extractingJsonPathStringValue("$.address").isEqualTo("123 Main St");
  }

  @Test
  void testCustomerAddedEventDeserialization() throws Exception {
    String content = "{\"id\":\"c1\",\"name\":\"John\",\"address\":\"123 Main St\"}";
    CustomerAddedEvent event = customerAddedEventJson.parseObject(content);
    assertThat(event.getId()).isEqualTo("c1");
    assertThat(event.getName()).isEqualTo("John");
    assertThat(event.getAddress()).isEqualTo("123 Main St");
  }

  @Test
  void testCustomerUpdatedEventSerialization() throws Exception {
    CustomerUpdatedEvent event = new CustomerUpdatedEvent("c1", "John Updated", "789 Pine Rd");
    JsonContent<CustomerUpdatedEvent> json = customerUpdatedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("c1");
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("John Updated");
  }

  @Test
  void testCustomerDeletedEventSerialization() throws Exception {
    CustomerDeletedEvent event = new CustomerDeletedEvent("c1");
    JsonContent<CustomerDeletedEvent> json = customerDeletedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("c1");
  }

  @Test
  void testRestaurantAddedEventSerialization() throws Exception {
    RestaurantAddedEvent event = new RestaurantAddedEvent("r1", "Pizza Hut");
    JsonContent<RestaurantAddedEvent> json = restaurantAddedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("r1");
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pizza Hut");
  }

  @Test
  void testRestaurantUpdatedEventSerialization() throws Exception {
    RestaurantUpdatedEvent event = new RestaurantUpdatedEvent("r1", "Pizza Hut Updated");
    JsonContent<RestaurantUpdatedEvent> json = restaurantUpdatedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pizza Hut Updated");
  }

  @Test
  void testRestaurantDeletedEventSerialization() throws Exception {
    RestaurantDeletedEvent event = new RestaurantDeletedEvent("r1");
    JsonContent<RestaurantDeletedEvent> json = restaurantDeletedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("r1");
  }

  @Test
  void testRestaurantDishAddedEventSerialization() throws Exception {
    RestaurantDishAddedEvent event =
        new RestaurantDishAddedEvent("r1", "d1", "Pizza Margherita", new BigDecimal("6.99"));
    JsonContent<RestaurantDishAddedEvent> json = restaurantDishAddedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.restaurantId").isEqualTo("r1");
    assertThat(json).extractingJsonPathStringValue("$.dishId").isEqualTo("d1");
    assertThat(json).extractingJsonPathStringValue("$.dishName").isEqualTo("Pizza Margherita");
  }

  @Test
  void testRestaurantDishUpdatedEventSerialization() throws Exception {
    RestaurantDishUpdatedEvent event =
        new RestaurantDishUpdatedEvent("r1", "d1", "Pizza Margherita 35cm", new BigDecimal("7.99"));
    JsonContent<RestaurantDishUpdatedEvent> json = restaurantDishUpdatedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.dishName").isEqualTo("Pizza Margherita 35cm");
  }

  @Test
  void testRestaurantDishDeletedEventSerialization() throws Exception {
    RestaurantDishDeletedEvent event = new RestaurantDishDeletedEvent("r1", "d1");
    JsonContent<RestaurantDishDeletedEvent> json = restaurantDishDeletedEventJson.write(event);
    assertThat(json).extractingJsonPathStringValue("$.restaurantId").isEqualTo("r1");
    assertThat(json).extractingJsonPathStringValue("$.dishId").isEqualTo("d1");
  }
}
