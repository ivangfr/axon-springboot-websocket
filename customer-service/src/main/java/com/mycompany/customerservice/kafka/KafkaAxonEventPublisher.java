package com.mycompany.customerservice.kafka;

import com.mycompany.axoneventcommons.customer.CustomerEvent;
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
    public void handle(CustomerEvent event) {
        GenericEventMessage<CustomerEvent> eventMessage = new GenericEventMessage<>(event);
        kafkaPublisher.send(eventMessage);
        log.info("[K]=> Publishing a Kafka message: {}", eventMessage);
    }

}
