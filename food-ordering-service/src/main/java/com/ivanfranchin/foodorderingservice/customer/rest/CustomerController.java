package com.ivanfranchin.foodorderingservice.customer.rest;

import com.ivanfranchin.foodorderingservice.customer.rest.dto.CustomerResponse;
import com.ivanfranchin.foodorderingservice.customer.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping
  public List<CustomerResponse> getCustomers() {
    return customerService.getCustomers().stream().map(CustomerResponse::from).toList();
  }
}
