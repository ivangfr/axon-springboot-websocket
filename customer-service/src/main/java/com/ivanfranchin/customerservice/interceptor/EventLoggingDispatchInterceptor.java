package com.ivanfranchin.customerservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;

import java.util.List;
import java.util.function.BiFunction;

@Slf4j
public class EventLoggingDispatchInterceptor implements MessageDispatchInterceptor<EventMessage<?>> {

    @Override
    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(List<? extends EventMessage<?>> messages) {
        return (index, event) -> {
            log.info("[E]=> Publishing an event: {}.", event);
            return event;
        };
    }
}
