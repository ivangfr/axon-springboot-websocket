package com.ivanfranchin.customerservice.aggregate;

import com.ivanfranchin.axoneventcommons.customer.CustomerAddedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerDeletedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerUpdatedEvent;
import com.ivanfranchin.customerservice.command.AddCustomerCommand;
import com.ivanfranchin.customerservice.command.DeleteCustomerCommand;
import com.ivanfranchin.customerservice.command.UpdateCustomerCommand;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CustomerAggregateTest {

    private FixtureConfiguration<CustomerAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(CustomerAggregate.class);
    }

    @Test
    void testAddCustomer() {
        String id = UUID.randomUUID().toString();
        fixture.givenNoPriorActivity()
                .when(new AddCustomerCommand(id, "John", "123 Main St"))
                .expectEvents(new CustomerAddedEvent(id, "John", "123 Main St"));
    }

    @Test
    void testUpdateCustomer() {
        String id = UUID.randomUUID().toString();
        fixture.given(new CustomerAddedEvent(id, "John", "123 Main St"))
                .when(new UpdateCustomerCommand(id, "John Updated", "789 Pine Rd"))
                .expectEvents(new CustomerUpdatedEvent(id, "John Updated", "789 Pine Rd"));
    }

    @Test
    void testUpdateCustomer_whenAllFieldsNull_keepsOriginalValues() {
        String id = UUID.randomUUID().toString();
        fixture.given(new CustomerAddedEvent(id, "John", "123 Main St"))
                .when(new UpdateCustomerCommand(id, null, null))
                .expectEvents(new CustomerUpdatedEvent(id, "John", "123 Main St"));
    }

    @Test
    void testDeleteCustomer() {
        String id = UUID.randomUUID().toString();
        fixture.given(new CustomerAddedEvent(id, "John", "123 Main St"))
                .when(new DeleteCustomerCommand(id))
                .expectEvents(new CustomerDeletedEvent(id))
                .expectMarkedDeleted();
    }
}
