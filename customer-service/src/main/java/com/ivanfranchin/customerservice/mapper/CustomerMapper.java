package com.ivanfranchin.customerservice.mapper;

import com.ivanfranchin.customerservice.model.Customer;
import com.ivanfranchin.customerservice.model.Order;
import com.ivanfranchin.customerservice.rest.dto.CustomerDto;
import com.ivanfranchin.customerservice.rest.dto.CustomerOrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

    CustomerOrderDto toCustomerOrderDto(Order order);
}
