package com.mycompany.customerservice.repository;

import com.mycompany.axoneventcommons.customer.CustomerAddedEvent;
import com.mycompany.axoneventcommons.customer.CustomerDeletedEvent;
import com.mycompany.axoneventcommons.customer.CustomerUpdatedEvent;
import com.mycompany.customerservice.exception.CustomerNotFoundException;
import com.mycompany.customerservice.model.Customer;
import com.mycompany.customerservice.query.GetCustomerQuery;
import com.mycompany.customerservice.query.GetCustomersQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerRepositoryProjector {

    private final CustomerRepository customerRepository;

    @QueryHandler
    public List<Customer> handle(GetCustomersQuery query) {
        return customerRepository.findAll();
    }

    @QueryHandler
    public Customer handle(GetCustomerQuery query) {
        return customerRepository.findById(query.getId())
                .orElseThrow(() -> new CustomerNotFoundException(query.getId()));
    }

    @EventHandler
    public void handle(CustomerAddedEvent event) {
        Customer customer = new Customer();
        customer.setId(event.getId());
        customer.setName(event.getName());
        customer.setAddress(event.getAddress());
        customerRepository.save(customer);
    }

    @EventHandler
    public void handle(CustomerUpdatedEvent event) {
        customerRepository.findById(event.getId())
                .ifPresent(c -> {
                    c.setName(event.getName());
                    c.setAddress(event.getAddress());
                    customerRepository.save(c);
                });
    }

    @EventHandler
    public void handle(CustomerDeletedEvent event) {
        customerRepository.findById(event.getId()).ifPresent(customerRepository::delete);
    }

}
