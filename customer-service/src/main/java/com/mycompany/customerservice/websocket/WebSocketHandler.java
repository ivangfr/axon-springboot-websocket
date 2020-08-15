package com.mycompany.customerservice.websocket;

import com.mycompany.axoneventcommons.customer.CustomerAddedEvent;
import com.mycompany.axoneventcommons.customer.CustomerDeletedEvent;
import com.mycompany.axoneventcommons.customer.CustomerUpdatedEvent;
import com.mycompany.customerservice.model.Customer;
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
        simpMessagingTemplate.convertAndSend("/topic/customers/added", event);
    }

    @EventHandler
    public void handle(CustomerUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/customers/updated", event);
    }

    @EventHandler
    public void handle(CustomerDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        simpMessagingTemplate.convertAndSend("/topic/customers/deleted", event);
    }

}
