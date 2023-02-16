package com.ivanfranchin.restaurantservice.rest;

import com.ivanfranchin.restaurantservice.command.AddRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.AddRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.command.DeleteRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.DeleteRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.command.UpdateRestaurantCommand;
import com.ivanfranchin.restaurantservice.command.UpdateRestaurantDishCommand;
import com.ivanfranchin.restaurantservice.mapper.RestaurantMapper;
import com.ivanfranchin.restaurantservice.model.Dish;
import com.ivanfranchin.restaurantservice.model.Order;
import com.ivanfranchin.restaurantservice.model.Restaurant;
import com.ivanfranchin.restaurantservice.query.GetRestaurantDishQuery;
import com.ivanfranchin.restaurantservice.query.GetRestaurantOrdersQuery;
import com.ivanfranchin.restaurantservice.query.GetRestaurantQuery;
import com.ivanfranchin.restaurantservice.query.GetRestaurantsQuery;
import com.ivanfranchin.restaurantservice.rest.dto.AddRestaurantDishRequest;
import com.ivanfranchin.restaurantservice.rest.dto.AddRestaurantRequest;
import com.ivanfranchin.restaurantservice.rest.dto.DishResponse;
import com.ivanfranchin.restaurantservice.rest.dto.RestaurantOrderResponse;
import com.ivanfranchin.restaurantservice.rest.dto.RestaurantResponse;
import com.ivanfranchin.restaurantservice.rest.dto.UpdateRestaurantDishRequest;
import com.ivanfranchin.restaurantservice.rest.dto.UpdateRestaurantRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final RestaurantMapper restaurantMapper;

    @GetMapping
    public CompletableFuture<List<RestaurantResponse>> getRestaurants() {
        return queryGateway.query(new GetRestaurantsQuery(), ResponseTypes.multipleInstancesOf(Restaurant.class))
                .thenApply(restaurants -> restaurants.stream().map(restaurantMapper::toRestaurantResponse).toList());
    }

    @GetMapping("/{id}")
    public CompletableFuture<RestaurantResponse> getRestaurant(@PathVariable UUID id) {
        return queryGateway.query(new GetRestaurantQuery(id.toString()), Restaurant.class)
                .thenApply(restaurantMapper::toRestaurantResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompletableFuture<String> addRestaurant(@Valid @RequestBody AddRestaurantRequest request) {
        return commandGateway.send(new AddRestaurantCommand(UUID.randomUUID().toString(), request.getName()));
    }

    @PatchMapping("/{restaurantId}")
    public CompletableFuture<String> updateRestaurant(@PathVariable UUID restaurantId,
                                                      @Valid @RequestBody UpdateRestaurantRequest request) {
        return commandGateway.send(new UpdateRestaurantCommand(restaurantId.toString(), request.getName()));
    }

    @DeleteMapping("/{restaurantId}")
    public CompletableFuture<String> deleteRestaurant(@PathVariable UUID restaurantId) {
        return commandGateway.send(new DeleteRestaurantCommand(restaurantId.toString()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{restaurantId}/dishes")
    public CompletableFuture<String> addRestaurantDish(@PathVariable UUID restaurantId,
                                                       @Valid @RequestBody AddRestaurantDishRequest request) {
        return commandGateway.send(new AddRestaurantDishCommand(restaurantId.toString(), UUID.randomUUID().toString(),
                request.getName(), request.getPrice()));
    }

    @GetMapping("/{restaurantId}/dishes/{dishId}")
    public CompletableFuture<DishResponse> getRestaurantDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId) {
        return queryGateway.query(new GetRestaurantDishQuery(restaurantId.toString(), dishId.toString()), Dish.class)
                .thenApply(restaurantMapper::toDishResponse);
    }

    @PatchMapping("/{restaurantId}/dishes/{dishId}")
    public CompletableFuture<String> updateRestaurantDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId,
                                                          @Valid @RequestBody UpdateRestaurantDishRequest request) {
        return commandGateway.send(new UpdateRestaurantDishCommand(restaurantId.toString(), dishId.toString(),
                request.getName(), request.getPrice()));
    }

    @DeleteMapping("/{restaurantId}/dishes/{dishId}")
    public CompletableFuture<String> deleteRestaurantDish(@PathVariable UUID restaurantId, @PathVariable UUID dishId) {
        return commandGateway.send(new DeleteRestaurantDishCommand(restaurantId.toString(), dishId.toString()));
    }

    @GetMapping("/{restaurantId}/orders")
    public CompletableFuture<List<RestaurantOrderResponse>> getRestaurantOrders(@PathVariable UUID restaurantId) {
        return queryGateway.query(new GetRestaurantOrdersQuery(restaurantId.toString()), ResponseTypes.multipleInstancesOf(Order.class))
                .thenApply(restaurants -> restaurants.stream().map(restaurantMapper::toRestaurantOrderResponse).toList());
    }
}
