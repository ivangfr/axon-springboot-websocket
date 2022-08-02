package com.ivanfranchin.restaurantservice.websocket;

import com.ivanfranchin.axoneventcommons.restaurant.RestaurantAddedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDeletedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishAddedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishDeletedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantDishUpdatedEvent;
import com.ivanfranchin.axoneventcommons.restaurant.RestaurantUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebSocketHandler {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventHandler
    public void handle(RestaurantAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/restaurant/added", event);
    }

    @EventHandler
    public void handle(RestaurantUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/restaurant/updated", event);
    }

    @EventHandler
    public void handle(RestaurantDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/restaurant/deleted", event);
    }

    @EventHandler
    public void handle(RestaurantDishAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/restaurant/dish/added", event);
    }

    @EventHandler
    public void handle(RestaurantDishUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/restaurant/dish/updated", event);
    }

    @EventHandler
    public void handle(RestaurantDishDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/restaurant/dish/deleted", event);
    }
}
