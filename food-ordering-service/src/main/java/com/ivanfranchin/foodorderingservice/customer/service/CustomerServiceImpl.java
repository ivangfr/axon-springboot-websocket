package com.ivanfranchin.foodorderingservice.customer.service;

import com.ivanfranchin.foodorderingservice.customer.exception.CustomerNotFoundException;
import com.ivanfranchin.foodorderingservice.customer.model.Customer;
import com.ivanfranchin.foodorderingservice.customer.repository.CustomerRepository;
import java.util.List;
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
  public List<Customer> getCustomers() {
    return customerRepository.findAll();
  }
}
