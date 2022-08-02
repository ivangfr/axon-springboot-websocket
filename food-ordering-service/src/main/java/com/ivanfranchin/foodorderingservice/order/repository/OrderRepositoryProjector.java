package com.ivanfranchin.foodorderingservice.order.repository;

import com.ivanfranchin.axoneventcommons.order.OrderCreatedEvent;
import com.ivanfranchin.foodorderingservice.order.exception.OrderNotFoundException;
import com.ivanfranchin.foodorderingservice.order.model.Order;
import com.ivanfranchin.foodorderingservice.order.model.OrderItem;
import com.ivanfranchin.foodorderingservice.order.model.OrderStatus;
import com.ivanfranchin.foodorderingservice.order.query.GetOrderQuery;
import com.ivanfranchin.foodorderingservice.order.query.GetOrdersQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderRepositoryProjector {

    private final OrderRepository orderRepository;

    @QueryHandler
    public List<Order> handle(GetOrdersQuery query) {
        return orderRepository.findAll();
    }

    @QueryHandler
    public Order handle(GetOrderQuery query) {
        return orderRepository.findById(query.getId()).orElseThrow(() -> new OrderNotFoundException(query.getId()));
    }

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        Order order = new Order();
        order.setId(event.getId());
        order.setCustomerId(event.getCustomerId());
        order.setCustomerName(event.getCustomerName());
        order.setCustomerAddress(event.getCustomerAddress());
        order.setRestaurantId(event.getRestaurantId());
        order.setRestaurantName(event.getRestaurantName());
        order.setStatus(OrderStatus.valueOf(event.getStatus()));
        order.setTotal(event.getTotal());
        order.setCreatedAt(event.getCreatedAt());
        order.setItems(event.getItems().stream()
                .map(i -> new OrderItem(i.getDishId(), i.getDishName(), i.getDishPrice(), i.getQuantity()))
                .collect(Collectors.toSet()));
        orderRepository.save(order);
    }
}
