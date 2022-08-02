package com.ivanfranchin.customerservice.mapper;

import com.ivanfranchin.customerservice.model.Customer;
import com.ivanfranchin.customerservice.model.Order;
import com.ivanfranchin.customerservice.rest.dto.CustomerResponse;
import com.ivanfranchin.customerservice.rest.dto.CustomerOrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);

    CustomerOrderResponse toCustomerOrderResponse(Order order);
}
