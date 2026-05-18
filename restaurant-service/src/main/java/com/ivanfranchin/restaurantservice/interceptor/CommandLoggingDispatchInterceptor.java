package com.ivanfranchin.restaurantservice.interceptor;

import java.util.List;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;

@Slf4j
public class CommandLoggingDispatchInterceptor
    implements MessageDispatchInterceptor<CommandMessage<?>> {

  @Override
  public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
      List<? extends CommandMessage<?>> messages) {
    return (index, command) -> {
      log.info("[C]=> Dispatching a command: {}.", command);
      return command;
    };
  }
}
