package com.ivanfranchin.foodorderingservice.customer.repository;

import com.ivanfranchin.axoneventcommons.customer.CustomerAddedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerDeletedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerUpdatedEvent;
import com.ivanfranchin.foodorderingservice.customer.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerRepositoryProjector {

    private final CustomerRepository customerRepository;

    @EventHandler
    public void handle(CustomerAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        Customer customer = new Customer();
        customer.setId(event.getId());
        customer.setName(event.getName());
        customer.setAddress(event.getAddress());
        customerRepository.save(customer);
    }

    @EventHandler
    public void handle(CustomerUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        customerRepository.findById(event.getId()).ifPresent(c -> {
            c.setName(event.getName() == null ? c.getName() : event.getName());
            c.setAddress(event.getAddress() == null ? c.getAddress() : event.getAddress());
            customerRepository.save(c);
        });
    }

    @EventHandler
    public void handle(CustomerDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        customerRepository.findById(event.getId()).ifPresent(customerRepository::delete);
    }
}
