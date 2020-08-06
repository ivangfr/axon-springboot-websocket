package com.mycompany.foodorderingservice.order.repository;

import com.mycompany.axoneventcommons.order.OrderItemAddedEvent;
import com.mycompany.axoneventcommons.order.OrderItemDeletedEvent;
import com.mycompany.axoneventcommons.order.OrderOpenedEvent;
import com.mycompany.axoneventcommons.order.OrderSubmittedEvent;
import com.mycompany.foodorderingservice.order.exception.OrderNotFoundException;
import com.mycompany.foodorderingservice.order.model.Order;
import com.mycompany.foodorderingservice.order.model.OrderItem;
import com.mycompany.foodorderingservice.order.model.OrderStatus;
import com.mycompany.foodorderingservice.order.query.GetOrderQuery;
import com.mycompany.foodorderingservice.order.query.GetOrdersQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void handle(OrderOpenedEvent event) {
        Order order = new Order();
        order.setId(event.getOrderId());
        order.setCustomerId(event.getCustomerId());
        order.setCustomerName(event.getCustomerName());
        order.setCustomerAddress(event.getCustomerAddress());
        order.setRestaurantId(event.getRestaurantId());
        order.setRestaurantName(event.getRestaurantName());
        order.setStatus(OrderStatus.valueOf(event.getStatus()));
        orderRepository.save(order);
    }

    @EventHandler
    public void handle(OrderSubmittedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(o -> {
            o.setStatus(OrderStatus.valueOf(event.getStatus()));
            orderRepository.save(o);
        });
    }

    @EventHandler
    public void handle(OrderItemAddedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(o -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(event.getItemId());
            orderItem.setDishId(event.getDishId());
            orderItem.setDishName(event.getDishName());
            orderItem.setDishPrice(event.getDishPrice());
            orderItem.setQuantity(event.getQuantity());
            orderItem.setOrder(o);
            o.getItems().add(orderItem);
            o.setTotal(orderItem.getDishPrice() * orderItem.getQuantity());
            orderRepository.save(o);
        });
    }

    @EventHandler
    public void handle(OrderItemDeletedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(o -> {
            Optional<OrderItem> orderItemOptional = o.getItems().stream()
                    .filter(i -> i.getId().equals(event.getItemId())).findAny();
            if (orderItemOptional.isPresent()) {
                OrderItem orderItem = orderItemOptional.get();
                o.setTotal(o.getTotal() - orderItem.getDishPrice() * orderItem.getQuantity());
                o.getItems().remove(orderItem);
            }
            orderRepository.save(o);
        });
    }

}
