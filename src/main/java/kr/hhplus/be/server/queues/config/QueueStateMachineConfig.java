package kr.hhplus.be.server.queues.config;

import kr.hhplus.be.server.queues.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineBuilder;

@Configuration
@EnableStateMachineFactory
public class QueueStateMachineConfig {
  @Bean
  public StateMachine<QueueStatus, QueueEvent> stateMachine() throws Exception {
    StateMachineBuilder.Builder<QueueStatus, QueueEvent> builder = StateMachineBuilder.builder();

    builder.configureStates()
        .withStates()
        .initial(QueueStatus.WAITING)
        .state(QueueStatus.PROCESSING)
        .end(QueueStatus.COMPLETED);

    builder.configureTransitions()
        .withExternal()
          .source(QueueStatus.WAITING)
          .target(QueueStatus.PROCESSING)
          .event(QueueEvent.PROCESS)
        .and()
        .withExternal()
          .source(QueueStatus.PROCESSING)
          .target(QueueStatus.COMPLETED)
          .event(QueueEvent.COMPLETE);

    return builder.build();
  }

}
