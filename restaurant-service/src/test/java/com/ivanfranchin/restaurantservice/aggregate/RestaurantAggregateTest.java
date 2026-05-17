package com.ivanfranchin.restaurantservice.aggregate;

import com.ivanfranchin.axoneventcommons.restaurant.RestaurantAddedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDeletedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishAddedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishDeletedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishUpdatedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantUpdatedEvent;
import com.ivanfranchin.restaurantservice.command.AddRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.AddRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.command.DeleteRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.DeleteRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.command.UpdateRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.UpdateRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.exception.DishNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class RestaurantAggregateTest {

    private FixtureConfiguration<RestaurantAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(RestaurantAggregate.class);
    }

    @Test
    void testAddRestaurant() {
        String id = UUID.randomUUID().toString();
        fixture.givenNoPriorActivity()
                .when(new AddRestaurantCommand(id, "Pizza Hut"))
                .expectEvents(new RestaurantAddedEvent(id, "Pizza Hut"));
    }

    @Test
    void testUpdateRestaurant() {
        String id = UUID.randomUUID().toString();
        fixture.given(new RestaurantAddedEvent(id, "Pizza Hut"))
                .when(new UpdateRestaurantCommand(id, "Pizza Hut Updated"))
                .expectEvents(new RestaurantUpdatedEvent(id, "Pizza Hut Updated"));
    }

    @Test
    void testDeleteRestaurant() {
        String id = UUID.randomUUID().toString();
        fixture.given(new RestaurantAddedEvent(id, "Pizza Hut"))
                .when(new DeleteRestaurantCommand(id))
                .expectEvents(new RestaurantDeletedEvent(id))
                .expectMarkedDeleted();
    }

    @Test
    void testAddRestaurantDish() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();
        fixture.given(new RestaurantAddedEvent(restaurantId, "Pizza Hut"))
                .when(new AddRestaurantDishCommand(restaurantId, dishId, "Pizza Margherita", new BigDecimal("6.99")))
                .expectEvents(new RestaurantDishAddedEvent(restaurantId, dishId, "Pizza Margherita", new BigDecimal("6.99")));
    }

    @Disabled("Axon fixture state comparison issue with Java 25")
    @Test
    void testUpdateRestaurantDish() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();
        fixture.given(
                        new RestaurantAddedEvent(restaurantId, "Pizza Hut"),
                        new RestaurantDishAddedEvent(restaurantId, dishId, "Pizza Margherita", new BigDecimal("6.99")))
                .when(new UpdateRestaurantDishCommand(restaurantId, dishId, "Pizza Margherita 35cm", new BigDecimal("7.99")))
                .expectEvents(new RestaurantDishUpdatedEvent(restaurantId, dishId, "Pizza Margherita 35cm", new BigDecimal("7.99")));
    }

    @Disabled("Axon fixture state comparison issue with Java 25")
    @Test
    void testUpdateRestaurantDish_whenNameNull_keepsOriginalName() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();
        fixture.given(
                        new RestaurantAddedEvent(restaurantId, "Pizza Hut"),
                        new RestaurantDishAddedEvent(restaurantId, dishId, "Pizza Margherita", new BigDecimal("6.99")))
                .when(new UpdateRestaurantDishCommand(restaurantId, dishId, null, new BigDecimal("7.99")))
                .expectEvents(new RestaurantDishUpdatedEvent(restaurantId, dishId, "Pizza Margherita", new BigDecimal("7.99")));
    }

    @Disabled("Axon fixture state comparison issue with Java 25")
    @Test
    void testUpdateRestaurantDish_whenPriceNull_keepsOriginalPrice() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();
        fixture.given(
                        new RestaurantAddedEvent(restaurantId, "Pizza Hut"),
                        new RestaurantDishAddedEvent(restaurantId, dishId, "Pizza Margherita", new BigDecimal("6.99")))
                .when(new UpdateRestaurantDishCommand(restaurantId, dishId, "Pizza Margherita 35cm", null))
                .expectEvents(new RestaurantDishUpdatedEvent(restaurantId, dishId, "Pizza Margherita 35cm", new BigDecimal("6.99")));
    }

    @Test
    void testUpdateRestaurantDish_whenDishNotFound_throwsException() {
        String restaurantId = UUID.randomUUID().toString();
        fixture.given(new RestaurantAddedEvent(restaurantId, "Pizza Hut"))
                .when(new UpdateRestaurantDishCommand(restaurantId, "nonexistent", "Updated", new BigDecimal("9.99")))
                .expectException(DishNotFoundException.class);
    }

    @Test
    void testDeleteRestaurantDish() {
        String restaurantId = UUID.randomUUID().toString();
        String dishId = UUID.randomUUID().toString();
        fixture.given(
                        new RestaurantAddedEvent(restaurantId, "Pizza Hut"),
                        new RestaurantDishAddedEvent(restaurantId, dishId, "Pizza Margherita", new BigDecimal("6.99")))
                .when(new DeleteRestaurantDishCommand(restaurantId, dishId))
                .expectEvents(new RestaurantDishDeletedEvent(restaurantId, dishId));
    }

    @Test
    void testDeleteRestaurantDish_whenDishNotFound_throwsException() {
        String restaurantId = UUID.randomUUID().toString();
        fixture.given(new RestaurantAddedEvent(restaurantId, "Pizza Hut"))
                .when(new DeleteRestaurantDishCommand(restaurantId, "nonexistent"))
                .expectException(DishNotFoundException.class);
    }
}
