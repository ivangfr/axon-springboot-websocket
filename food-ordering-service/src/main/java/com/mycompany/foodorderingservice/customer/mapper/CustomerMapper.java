package com.mycompany.foodorderingservice.customer.mapper;

import com.mycompany.foodorderingservice.customer.model.Customer;
import com.mycompany.foodorderingservice.customer.rest.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

}
