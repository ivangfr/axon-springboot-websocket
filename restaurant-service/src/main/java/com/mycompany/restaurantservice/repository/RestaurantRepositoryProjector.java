package com.mycompany.restaurantservice.repository;

import com.mycompany.axoneventcommons.order.OrderCreatedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantAddedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDeletedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDishAddedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDishDeletedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDishUpdatedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantUpdatedEvent;
import com.mycompany.restaurantservice.exception.DishNotFoundException;
import com.mycompany.restaurantservice.exception.RestaurantNotFoundException;
import com.mycompany.restaurantservice.model.Dish;
import com.mycompany.restaurantservice.model.Order;
import com.mycompany.restaurantservice.model.OrderItem;
import com.mycompany.restaurantservice.model.Restaurant;
import com.mycompany.restaurantservice.query.GetRestaurantDishQuery;
import com.mycompany.restaurantservice.query.GetRestaurantOrdersQuery;
import com.mycompany.restaurantservice.query.GetRestaurantQuery;
import com.mycompany.restaurantservice.query.GetRestaurantsQuery;
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
public class RestaurantRepositoryProjector {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;

    @QueryHandler
    public List<Restaurant> handle(GetRestaurantsQuery query) {
        return restaurantRepository.findAll();
    }

    @QueryHandler
    public Restaurant handle(GetRestaurantQuery query) {
        return restaurantRepository.findById(query.getId())
                .orElseThrow(() -> new RestaurantNotFoundException(query.getId()));
    }

    @QueryHandler
    public Dish handle(GetRestaurantDishQuery query) {
        return restaurantRepository.findById(query.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(query.getRestaurantId()))
                .getDishes()
                .stream()
                .filter(dish -> dish.getId().equals(query.getDishId()))
                .findAny()
                .orElseThrow(() -> new DishNotFoundException(query.getRestaurantId(), query.getDishId()));
    }

    @QueryHandler
    public List<Order> handle(GetRestaurantOrdersQuery query) {
        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(query.getId());
    }

    @EventHandler
    public void handle(RestaurantAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(event.getId());
        restaurant.setName(event.getName());
        restaurantRepository.save(restaurant);
    }

    @EventHandler
    public void handle(RestaurantUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getId()).ifPresent(r -> {
            r.setName(event.getName());
            restaurantRepository.save(r);
        });
    }

    @EventHandler
    public void handle(RestaurantDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getId()).ifPresent(restaurantRepository::delete);
    }

    @EventHandler
    public void handle(RestaurantDishAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r -> {
            Dish dish = new Dish();
            dish.setId(event.getDishId());
            dish.setName(event.getDishName());
            dish.setPrice(event.getDishPrice());
            dish.setRestaurant(r);
            r.getDishes().add(dish);
            restaurantRepository.save(r);
        });
    }

    @EventHandler
    public void handle(RestaurantDishUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r ->
                r.getDishes().stream().filter(d -> d.getId().equals(event.getDishId())).findAny()
                        .ifPresent(d -> {
                            d.setName(event.getDishName());
                            d.setPrice(event.getDishPrice());
                            restaurantRepository.save(r);
                        }));
    }

    @EventHandler
    public void handle(RestaurantDishDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r -> {
            r.getDishes().removeIf(d -> d.getId().equals(event.getDishId()));
            restaurantRepository.save(r);
        });
    }

    // -- Order Events

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r -> {
            Order order = new Order();
            order.setId(event.getOrderId());
            order.setCustomerName(event.getCustomerName());
            order.setCustomerAddress(event.getCustomerAddress());
            order.setStatus(event.getStatus());
            order.setTotal(event.getTotal());
            order.setCreatedAt(event.getCreatedAt());
            order.setItems(event.getItems().stream()
                    .map(i -> new OrderItem(i.getDishId(), i.getDishName(), i.getDishPrice(), i.getQuantity()))
                    .collect(Collectors.toSet()));
            order.setRestaurant(r);
            r.getOrders().add(order);
            restaurantRepository.save(r);
        });
    }

}
