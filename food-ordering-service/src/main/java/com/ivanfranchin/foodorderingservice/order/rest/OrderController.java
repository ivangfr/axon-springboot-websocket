package com.ivanfranchin.foodorderingservice.order.rest;

import com.ivanfranchin.foodorderingservice.order.model.Order;
import com.ivanfranchin.foodorderingservice.order.query.GetOrderQuery;
import com.ivanfranchin.foodorderingservice.order.query.GetOrdersQuery;
import com.ivanfranchin.foodorderingservice.order.rest.dto.CreateOrderRequest;
import com.ivanfranchin.foodorderingservice.order.rest.dto.OrderResponse;
import com.ivanfranchin.foodorderingservice.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final QueryGateway queryGateway;
  private final OrderService orderService;

  @GetMapping
  public CompletableFuture<List<OrderResponse>> getOrders() {
    return queryGateway
        .query(new GetOrdersQuery(), ResponseTypes.multipleInstancesOf(Order.class))
        .thenApply(orders -> orders.stream().map(OrderResponse::from).toList());
  }

  @GetMapping("/{id}")
  public CompletableFuture<OrderResponse> getOrder(@PathVariable UUID id) {
    return queryGateway
        .query(new GetOrderQuery(id.toString()), Order.class)
        .thenApply(OrderResponse::from);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public CompletableFuture<String> createOrder(@Valid @RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request);
  }
}
