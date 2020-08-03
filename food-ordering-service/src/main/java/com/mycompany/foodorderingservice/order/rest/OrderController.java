package com.mycompany.foodorderingservice.order.rest;

import com.mycompany.foodorderingservice.order.command.AddOrderItemCommand;
import com.mycompany.foodorderingservice.order.command.DeleteOrderItemCommand;
import com.mycompany.foodorderingservice.order.command.OpenOrderCommand;
import com.mycompany.foodorderingservice.order.mapper.OrderMapper;
import com.mycompany.foodorderingservice.order.model.Order;
import com.mycompany.foodorderingservice.order.query.GetOrderQuery;
import com.mycompany.foodorderingservice.order.query.GetOrdersQuery;
import com.mycompany.foodorderingservice.order.rest.dto.AddOrderItemRequest;
import com.mycompany.foodorderingservice.order.rest.dto.OpenOrderRequest;
import com.mycompany.foodorderingservice.order.rest.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final OrderMapper orderMapper;

    @GetMapping
    public CompletableFuture<List<OrderDto>> getOrders() {
        return queryGateway.query(new GetOrdersQuery(), ResponseTypes.multipleInstancesOf(Order.class))
                .thenApply(orders -> orders.stream()
                        .map(orderMapper::toOrderDto).collect(Collectors.toList()));
    }

    @GetMapping("/{orderId}")
    public CompletableFuture<OrderDto> getOrder(@PathVariable UUID orderId) {
        return queryGateway.query(new GetOrderQuery(orderId.toString()), Order.class)
                .thenApply(orderMapper::toOrderDto);
    }

    @PostMapping
    public CompletableFuture<String> openOrder(@Valid @RequestBody OpenOrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        return commandGateway.send(new OpenOrderCommand(orderId, request.getCustomerId(), request.getRestaurantId()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{orderId}/items")
    public CompletableFuture<String> addOrderItem(@PathVariable UUID orderId,
                                                  @Valid @RequestBody AddOrderItemRequest request) {
        Float dishPrice = 10.99f;
        return commandGateway.send(new AddOrderItemCommand(orderId.toString(), request.getDishId(), dishPrice, request.getQuantity()));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public CompletableFuture<String> deleteOrderItem(@PathVariable UUID orderId, @PathVariable UUID itemId) {
        return commandGateway.send(new DeleteOrderItemCommand(orderId.toString(), itemId.toString()));
    }

}
