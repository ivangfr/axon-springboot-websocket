package com.mycompany.customerservice.repository;

import com.mycompany.customerservice.event.CustomerAddedEvent;
import com.mycompany.customerservice.event.CustomerDeletedEvent;
import com.mycompany.customerservice.event.CustomerUpdatedEvent;
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
    public List<Customer> getRestaurant(GetCustomersQuery query) {
        return customerRepository.findAll();
    }

    @QueryHandler
    public Customer getRestaurant(GetCustomerQuery query) {
        return customerRepository.findById(query.getId())
                .orElseThrow(() -> new CustomerNotFoundException(query.getId()));
    }

    @EventHandler
    public void addCustomer(CustomerAddedEvent event) {
        Customer customer = new Customer();
        customer.setId(event.getId());
        customer.setName(event.getName());
        customer.setAddress(event.getAddress());
        customerRepository.save(customer);
    }

    @EventHandler
    public void updateCustomer(CustomerUpdatedEvent event) {
        customerRepository.findById(event.getId())
                .ifPresent(c -> {
                    c.setName(event.getName() == null ? c.getName() : event.getName());
                    c.setAddress(event.getAddress() == null ? c.getAddress() : event.getAddress());
                    customerRepository.save(c);
                });
    }

    @EventHandler
    public void deleteCustomer(CustomerDeletedEvent event) {
        customerRepository.findById(event.getId()).ifPresent(customerRepository::delete);
    }

}
