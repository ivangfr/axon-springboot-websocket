package com.mycompany.foodorderingservice.customer.service;

import com.mycompany.foodorderingservice.customer.exception.CustomerNotFoundException;
import com.mycompany.foodorderingservice.customer.model.Customer;
import com.mycompany.foodorderingservice.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer validateAndGetCustomer(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
