package com.ivanfranchin.customerservice.websocket;

import com.ivanfranchin.axoneventcommons.customer.CustomerAddedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerDeletedEvent;
import com.ivanfranchin.axoneventcommons.customer.CustomerUpdatedEvent;
import com.ivanfranchin.axoneventcommons.order.OrderCreatedEvent;
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
    public void handle(CustomerAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/customer/added", event);
    }

    @EventHandler
    public void handle(CustomerUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/customer/updated", event);
    }

    @EventHandler
    public void handle(CustomerDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/customer/deleted", event);
    }

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/customer/order/created", event);
    }
}
