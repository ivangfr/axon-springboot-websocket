package com.ivanfranchin.restaurantservice.rest.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RestaurantDtoJsonTest {

    @Autowired
    private JacksonTester<AddRestaurantRequest> addRestaurantRequestJson;

    @Autowired
    private JacksonTester<UpdateRestaurantRequest> updateRestaurantRequestJson;

    @Autowired
    private JacksonTester<AddRestaurantDishRequest> addRestaurantDishRequestJson;

    @Autowired
    private JacksonTester<UpdateRestaurantDishRequest> updateRestaurantDishRequestJson;

    @Autowired
    private JacksonTester<RestaurantResponse> restaurantResponseJson;

    @Autowired
    private JacksonTester<DishResponse> dishResponseJson;

    @Autowired
    private JacksonTester<RestaurantOrderResponse> restaurantOrderResponseJson;

    @Test
    void testAddRestaurantRequestSerialization() throws Exception {
        AddRestaurantRequest request = new AddRestaurantRequest("Pizza Hut");
        JsonContent<AddRestaurantRequest> json = addRestaurantRequestJson.write(request);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pizza Hut");
    }

    @Test
    void testAddRestaurantRequestDeserialization() throws Exception {
        String content = "{\"name\":\"McDonald's\"}";
        AddRestaurantRequest request = addRestaurantRequestJson.parseObject(content);
        assertThat(request.name()).isEqualTo("McDonald's");
    }

    @Test
    void testUpdateRestaurantRequestSerialization() throws Exception {
        UpdateRestaurantRequest request = new UpdateRestaurantRequest("McDonald's");
        JsonContent<UpdateRestaurantRequest> json = updateRestaurantRequestJson.write(request);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("McDonald's");
    }

    @Test
    void testAddRestaurantDishRequestSerialization() throws Exception {
        AddRestaurantDishRequest request = new AddRestaurantDishRequest("Pizza Margherita", new BigDecimal("6.99"));
        JsonContent<AddRestaurantDishRequest> json = addRestaurantDishRequestJson.write(request);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pizza Margherita");
        assertThat(json).extractingJsonPathNumberValue("$.price").isEqualTo(6.99);
    }

    @Test
    void testUpdateRestaurantDishRequestSerialization() throws Exception {
        UpdateRestaurantDishRequest request = new UpdateRestaurantDishRequest("Pizza Margherita 35cm", new BigDecimal("7.99"));
        JsonContent<UpdateRestaurantDishRequest> json = updateRestaurantDishRequestJson.write(request);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pizza Margherita 35cm");
    }

    @Test
    void testRestaurantResponseSerialization() throws Exception {
        RestaurantResponse.Dish dish = new RestaurantResponse.Dish("d1", "Pizza Margherita", new BigDecimal("6.99"));
        RestaurantResponse response = new RestaurantResponse("r1", "Pizza Hut", Set.of(dish));
        JsonContent<RestaurantResponse> json = restaurantResponseJson.write(response);
        assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("r1");
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Pizza Hut");
        assertThat(json).extractingJsonPathStringValue("$.dishes[0].id").isEqualTo("d1");
    }

    @Test
    void testDishResponseSerialization() throws Exception {
        DishResponse response = new DishResponse("r1", "d1", "Pizza Margherita", new BigDecimal("6.99"));
        JsonContent<DishResponse> json = dishResponseJson.write(response);
        assertThat(json).extractingJsonPathStringValue("$.restaurantId").isEqualTo("r1");
        assertThat(json).extractingJsonPathStringValue("$.dishId").isEqualTo("d1");
        assertThat(json).extractingJsonPathStringValue("$.dishName").isEqualTo("Pizza Margherita");
        assertThat(json).extractingJsonPathNumberValue("$.dishPrice").isEqualTo(6.99);
    }

    @Test
    void testRestaurantOrderResponseSerialization() throws Exception {
        RestaurantOrderResponse.OrderItem item = new RestaurantOrderResponse.OrderItem(
                "d1", "Pizza Margherita", new BigDecimal("6.99"), (short) 2);
        RestaurantOrderResponse response = new RestaurantOrderResponse("o1", "John", "123 Main St",
                "CREATED", new BigDecimal("13.98"), ZonedDateTime.parse("2025-01-15T10:30:00Z"), Set.of(item));
        JsonContent<RestaurantOrderResponse> json = restaurantOrderResponseJson.write(response);
        assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo("o1");
        assertThat(json).extractingJsonPathStringValue("$.customerName").isEqualTo("John");
        assertThat(json).extractingJsonPathStringValue("$.items[0].dishId").isEqualTo("d1");
    }
}
