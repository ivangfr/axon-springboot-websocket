package com.mycompany.restaurantservice.kafka;

import com.mycompany.axoneventcommons.restaurant.RestaurantEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaAxonEventPublisher {

    private final KafkaPublisher<String, byte[]> kafkaPublisher;

    @EventHandler
    public void handle(RestaurantEvent event) {
        GenericEventMessage<RestaurantEvent> eventMessage = new GenericEventMessage<>(event);
        kafkaPublisher.send(eventMessage);
        log.info("[K]=> Publishing a Kafka message: {}", eventMessage);
    }

}
