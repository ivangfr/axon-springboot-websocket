package com.ivanfranchin.foodorderingservice.order.aggregate;

import com.ivanfranchin.foodorderingservice.order.command.CreateOrderCommand;
import com.ivanfranchin.foodorderingservice.order.model.OrderItem;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.axonframework.test.matchers.Matchers.exactSequenceOf;
import static org.axonframework.test.matchers.Matchers.messageWithPayload;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

class OrderAggregateTest {

    private FixtureConfiguration<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void testCreateOrder() {
        String id = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        String restaurantId = UUID.randomUUID().toString();

        Set<OrderItem> items = new LinkedHashSet<>();
        items.add(new OrderItem("d1", "Pizza Margherita", new BigDecimal("10.00"), (short) 2));
        items.add(new OrderItem("d2", "Coke", new BigDecimal("2.50"), (short) 3));

        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(id, customerId, "John", "123 Main St",
                        restaurantId, "Pizza Hut", items))
                .expectEventsMatching(exactSequenceOf(
                        messageWithPayload(allOf(
                                hasProperty("id", is(id)),
                                hasProperty("customerId", is(customerId)),
                                hasProperty("customerName", is("John")),
                                hasProperty("customerAddress", is("123 Main St")),
                                hasProperty("restaurantId", is(restaurantId)),
                                hasProperty("restaurantName", is("Pizza Hut")),
                                hasProperty("status", is("CREATED")),
                                hasProperty("total", is(new BigDecimal("27.50")))
                        ))
                ));
    }

    @Test
    void testCreateOrder_withZeroTotal() {
        String id = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        String restaurantId = UUID.randomUUID().toString();

        Set<OrderItem> items = new LinkedHashSet<>();

        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(id, customerId, "John", "123 Main St",
                        restaurantId, "Pizza Hut", items))
                .expectEventsMatching(exactSequenceOf(
                        messageWithPayload(allOf(
                                hasProperty("id", is(id)),
                                hasProperty("total", is(BigDecimal.ZERO)),
                                hasProperty("status", is("CREATED"))
                        ))
                ));
    }
}
