package com.ivanfranchin.restaurantservice.config;

import com.ivanfranchin.restaurantservice.interceptor.CommandLoggingDispatchInterceptor;
import com.ivanfranchin.restaurantservice.interceptor.EventLoggingDispatchInterceptor;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Configuration
public class AxonConfig {

    private final CommandBus commandBus;
    private final EventBus eventBus;

    @PostConstruct
    public void init() {
        commandBus.registerDispatchInterceptor(new CommandLoggingDispatchInterceptor());
        eventBus.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
    }
}
