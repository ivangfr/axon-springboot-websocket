package com.ivanfranchin.foodorderingservice.order.service;

import com.ivanfranchin.foodorderingservice.customer.model.Customer;
import com.ivanfranchin.foodorderingservice.customer.service.CustomerService;
import com.ivanfranchin.foodorderingservice.order.command.CreateOrderCommand;
import com.ivanfranchin.foodorderingservice.order.model.OrderItem;
import com.ivanfranchin.foodorderingservice.order.rest.dto.CreateOrderRequest;
import com.ivanfranchin.foodorderingservice.restaurant.model.Dish;
import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import com.ivanfranchin.foodorderingservice.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final CommandGateway commandGateway;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public CompletableFuture<String> createOrder(CreateOrderRequest request) {
        Customer customer = customerService.validateAndGetCustomer(request.customerId().toString());
        Restaurant restaurant = restaurantService.validateAndGetRestaurant(request.restaurantId().toString());
        Set<OrderItem> items = request.items().stream().map(i -> {
            Dish dish = restaurantService.validateAndGetRestaurantDish(restaurant.getId(), i.dishId().toString());
            return new OrderItem(dish.getId(), dish.getName(), dish.getPrice(), i.quantity());
        }).collect(Collectors.toSet());

        String id = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(id, customer.getId(), customer.getName(),
                customer.getAddress(), restaurant.getId(), restaurant.getName(), items));
    }
}
