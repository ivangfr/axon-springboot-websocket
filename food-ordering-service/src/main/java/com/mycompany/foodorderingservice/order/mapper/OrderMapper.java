package com.mycompany.foodorderingservice.order.mapper;

import com.mycompany.foodorderingservice.order.model.Order;
import com.mycompany.foodorderingservice.order.rest.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);
}
