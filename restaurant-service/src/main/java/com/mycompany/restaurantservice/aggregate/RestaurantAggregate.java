package com.mycompany.restaurantservice.aggregate;

import com.mycompany.restaurantservice.command.AddRestaurantCommand;
import com.mycompany.restaurantservice.command.AddRestaurantDishCommand;
import com.mycompany.restaurantservice.command.DeleteRestaurantCommand;
import com.mycompany.restaurantservice.command.DeleteRestaurantDishCommand;
import com.mycompany.restaurantservice.command.UpdateRestaurantCommand;
import com.mycompany.restaurantservice.event.RestaurantAddedEvent;
import com.mycompany.restaurantservice.event.RestaurantDeletedEvent;
import com.mycompany.restaurantservice.event.RestaurantDishAddedEvent;
import com.mycompany.restaurantservice.event.RestaurantDishDeletedEvent;
import com.mycompany.restaurantservice.event.RestaurantUpdatedEvent;
import com.mycompany.restaurantservice.exception.DishNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Aggregate
public class RestaurantAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private Set<Dish> dishes;

    // -- Add Restaurant

    @CommandHandler
    public RestaurantAggregate(AddRestaurantCommand command) {
        AggregateLifecycle.apply(new RestaurantAddedEvent(command.getId(), command.getName()));
    }

    @EventSourcingHandler
    public void on(RestaurantAddedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.dishes = new LinkedHashSet<>();
    }

    // -- Update Restaurant

    @CommandHandler
    public void handle(UpdateRestaurantCommand command) {
        AggregateLifecycle.apply(new RestaurantUpdatedEvent(command.getId(), command.getName()));
    }

    @EventSourcingHandler
    public void on(RestaurantUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
    }

    // -- Delete Restaurant

    @CommandHandler
    public void handle(DeleteRestaurantCommand command) {
        AggregateLifecycle.apply(new RestaurantDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(RestaurantDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }

    // -- Add Restaurant Dish

    @CommandHandler
    public void handle(AddRestaurantDishCommand command) {
        AggregateLifecycle.apply(new RestaurantDishAddedEvent(command.getRestaurantId(), command.getDishId(),
                command.getDishName(), command.getDishPrice()));
    }

    @EventSourcingHandler
    public void on(RestaurantDishAddedEvent event) {
        this.id = event.getRestaurantId();
        this.dishes.add(new Dish(event.getDishId(), event.getDishName(), event.getDishPrice()));
    }

    // -- Delete Restaurant Dish

    @CommandHandler
    public void handle(DeleteRestaurantDishCommand command) {
        if (this.dishes.stream().noneMatch(d -> d.getId().equals(command.getDishId()))) {
            throw new DishNotFoundException(command.getRestaurantId(), command.getDishId());
        }
        AggregateLifecycle.apply(new RestaurantDishDeletedEvent(command.getRestaurantId(), command.getDishId()));
    }

    @EventSourcingHandler
    public void on(RestaurantDishDeletedEvent event) {
        this.id = event.getRestaurantId();
        this.dishes.removeIf(d -> d.getId().equals(event.getDishId()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dish {

        private String id;
        private String name;
        private Float price;
    }

}
