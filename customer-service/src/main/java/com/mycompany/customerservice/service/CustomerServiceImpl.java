package com.mycompany.customerservice.service;

import com.mycompany.customerservice.exception.CustomerNotFoundException;
import com.mycompany.customerservice.model.Customer;
import com.mycompany.customerservice.repository.CustomerRepository;
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

}
