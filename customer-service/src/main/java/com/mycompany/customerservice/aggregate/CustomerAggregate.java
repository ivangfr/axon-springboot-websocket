package com.mycompany.customerservice.aggregate;

import com.mycompany.customerservice.command.AddCustomerCommand;
import com.mycompany.customerservice.command.DeleteCustomerCommand;
import com.mycompany.customerservice.command.UpdateCustomerCommand;
import com.mycompany.customerservice.event.CustomerAddedEvent;
import com.mycompany.customerservice.event.CustomerDeletedEvent;
import com.mycompany.customerservice.event.CustomerUpdatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@NoArgsConstructor
@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private String address;

    // -- Add Customer

    @CommandHandler
    public CustomerAggregate(AddCustomerCommand command) {
        AggregateLifecycle.apply(new CustomerAddedEvent(command.getId(), command.getName(), command.getAddress()));
    }

    @EventSourcingHandler
    public void on(CustomerAddedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.address = event.getAddress();
    }

    // -- Update Customer

    @CommandHandler
    public void handle(UpdateCustomerCommand command) {
        AggregateLifecycle.apply(new CustomerUpdatedEvent(command.getId(), command.getName(), command.getAddress()));
    }

    @EventSourcingHandler
    public void on(CustomerUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.address = event.getAddress();
    }

    // -- Delete Customer

    @CommandHandler
    public void handle(DeleteCustomerCommand command) {
        AggregateLifecycle.apply(new CustomerDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(CustomerDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }

}
