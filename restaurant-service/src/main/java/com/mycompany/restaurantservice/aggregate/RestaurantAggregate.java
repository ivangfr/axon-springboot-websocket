package com.mycompany.restaurantservice.aggregate;

import com.mycompany.axoneventcommons.restaurant.RestaurantAddedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDeletedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDishAddedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDishDeletedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantDishUpdatedEvent;
import com.mycompany.axoneventcommons.restaurant.RestaurantUpdatedEvent;
import com.mycompany.axoneventcommons.util.MyStringUtils;
import com.mycompany.restaurantservice.command.AddRestaurantCommand;
import com.mycompany.restaurantservice.command.AddRestaurantDishCommand;
import com.mycompany.restaurantservice.command.DeleteRestaurantCommand;
import com.mycompany.restaurantservice.command.DeleteRestaurantDishCommand;
import com.mycompany.restaurantservice.command.UpdateRestaurantCommand;
import com.mycompany.restaurantservice.command.UpdateRestaurantDishCommand;
import com.mycompany.restaurantservice.exception.DishNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
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
    public void handle(RestaurantAddedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.dishes = new LinkedHashSet<>();
    }

    // -- Update Restaurant

    @CommandHandler
    public void handle(UpdateRestaurantCommand command) {
        String newName = MyStringUtils.getTrimmedValueOrElse(command.getName(), this.name);
        AggregateLifecycle.apply(new RestaurantUpdatedEvent(command.getId(), newName));
    }

    @EventSourcingHandler
    public void handle(RestaurantUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
    }

    // -- Delete Restaurant

    @CommandHandler
    public void handle(DeleteRestaurantCommand command) {
        AggregateLifecycle.apply(new RestaurantDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void handle(RestaurantDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }

    // -- Add Restaurant Dish

    @CommandHandler
    public void handle(AddRestaurantDishCommand command) {
        AggregateLifecycle.apply(new RestaurantDishAddedEvent(command.getRestaurantId(), command.getDishId(),
                command.getDishName(), command.getDishPrice()));
    }

    @EventSourcingHandler
    public void handle(RestaurantDishAddedEvent event) {
        this.id = event.getRestaurantId();
        this.dishes.add(new Dish(event.getDishId(), event.getDishName(), event.getDishPrice()));
    }

    // -- Update Restaurant Dish

    @CommandHandler
    public void handle(UpdateRestaurantDishCommand command) {
        this.dishes.stream().filter(d -> d.getId().equals(command.getDishId())).findAny().ifPresentOrElse(d -> {
            String newName = MyStringUtils.getTrimmedValueOrElse(command.getDishName(), d.getName());
            BigDecimal newPrice = command.getDishPrice() == null ? d.getPrice() : command.getDishPrice();
            AggregateLifecycle.apply(new RestaurantDishUpdatedEvent(command.getRestaurantId(), command.getDishId(), newName, newPrice));
        }, () -> {
            throw new DishNotFoundException(command.getRestaurantId(), command.getDishId());
        });
    }

    @EventSourcingHandler
    public void handle(RestaurantDishUpdatedEvent event) {
        this.id = event.getRestaurantId();
        this.dishes.stream().filter(d -> d.getId().equals(event.getDishId())).findAny().ifPresent(d -> {
            d.setName(event.getDishName());
            d.setPrice(event.getDishPrice());
        });
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
    public void handle(RestaurantDishDeletedEvent event) {
        this.id = event.getRestaurantId();
        this.dishes.removeIf(d -> d.getId().equals(event.getDishId()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dish {

        private String id;
        private String name;
        private BigDecimal price;
    }

}
