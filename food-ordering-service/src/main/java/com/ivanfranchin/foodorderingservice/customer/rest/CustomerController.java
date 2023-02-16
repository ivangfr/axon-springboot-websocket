package com.ivanfranchin.foodorderingservice.customer.rest;

import com.ivanfranchin.foodorderingservice.customer.mapper.CustomerMapper;
import com.ivanfranchin.foodorderingservice.customer.rest.dto.CustomerResponse;
import com.ivanfranchin.foodorderingservice.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping
    public List<CustomerResponse> getCustomers() {
        return customerService.getCustomers().stream().map(customerMapper::toCustomerResponse).toList();
    }
}
