package com.mycompany.customerservice.service;

import com.mycompany.customerservice.model.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    Customer validateAndGetCustomer(String id);

}
