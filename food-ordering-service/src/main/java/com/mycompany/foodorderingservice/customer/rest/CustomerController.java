package com.mycompany.foodorderingservice.customer.rest;

import com.mycompany.foodorderingservice.customer.mapper.CustomerMapper;
import com.mycompany.foodorderingservice.customer.rest.dto.CustomerDto;
import com.mycompany.foodorderingservice.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping
    public List<CustomerDto> getCustomers() {
        return customerService.getCustomers().stream()
                .map(customerMapper::toCustomerDto)
                .collect(Collectors.toList());
    }

}
