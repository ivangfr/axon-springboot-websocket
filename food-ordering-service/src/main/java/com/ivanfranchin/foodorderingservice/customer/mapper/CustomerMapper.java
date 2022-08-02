package com.ivanfranchin.foodorderingservice.customer.mapper;

import com.ivanfranchin.foodorderingservice.customer.model.Customer;
import com.ivanfranchin.foodorderingservice.customer.rest.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);
}
