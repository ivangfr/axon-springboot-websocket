package com.mycompany.customerservice.mapper;

import com.mycompany.customerservice.model.Customer;
import com.mycompany.customerservice.model.Order;
import com.mycompany.customerservice.rest.dto.CustomerDto;
import com.mycompany.customerservice.rest.dto.CustomerOrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

    CustomerOrderDto toCustomerOrderDto(Order order);

}
