package com.ivanfranchin.foodorderingservice.order.mapper;

import com.ivanfranchin.foodorderingservice.order.model.Order;
import com.ivanfranchin.foodorderingservice.order.rest.dto.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);
}
