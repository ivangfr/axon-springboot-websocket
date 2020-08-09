package com.mycompany.restaurantservice.rest;

import com.mycompany.restaurantservice.command.AddRestaurantCommand;
import com.mycompany.restaurantservice.command.AddRestaurantDishCommand;
import com.mycompany.restaurantservice.command.DeleteRestaurantCommand;
import com.mycompany.restaurantservice.command.DeleteRestaurantDishCommand;
import com.mycompany.restaurantservice.command.UpdateRestaurantCommand;
import com.mycompany.restaurantservice.command.UpdateRestaurantDishCommand;
import com.mycompany.restaurantservice.mapper.RestaurantMapper;
import com.mycompany.restaurantservice.model.Order;
import com.mycompany.restaurantservice.model.Restaurant;
import com.mycompany.restaurantservice.query.GetRestaurantOrdersQuery;
import com.mycompany.restaurantservice.query.GetRestaurantQuery;
import com.mycompany.restaurantservice.query.GetRestaurantsQuery;
import com.mycompany.restaurantservice.rest.dto.AddRestaurantDishRequest;
import com.mycompany.restaurantservice.rest.dto.AddRestaurantRequest;
import com.mycompany.restaurantservice.rest.dto.RestaurantDto;
import com.mycompany.restaurantservice.rest.dto.RestaurantOrderDto;
import com.mycompany.restaurantservice.rest.dto.UpdateRestaurantDishRequest;
import com.mycompany.restaurantservice.rest.dto.UpdateRestaurantRequest;
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

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final RestaurantMapper restaurantMapper;

    @GetMapping
    public CompletableFuture<List<RestaurantDto>> getRestaurants() {
        return queryGateway.query(new GetRestaurantsQuery(), ResponseTypes.multipleInstancesOf(Restaurant.class))
                .thenApply(restaurants -> restaurants.stream()
                        .map(restaurantMapper::toRestaurantDto)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public CompletableFuture<RestaurantDto> getRestaurant(@PathVariable UUID id) {
        return queryGateway.query(new GetRestaurantQuery(id.toString()), Restaurant.class)
                .thenApply(restaurantMapper::toRestaurantDto);
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
    public CompletableFuture<List<RestaurantOrderDto>> getRestaurantOrders(@PathVariable UUID restaurantId) {
        return queryGateway.query(new GetRestaurantOrdersQuery(restaurantId.toString()), ResponseTypes.multipleInstancesOf(Order.class))
                .thenApply(restaurants -> restaurants.stream()
                        .map(restaurantMapper::toRestaurantOrderDto)
                        .collect(Collectors.toList()));
    }

}
