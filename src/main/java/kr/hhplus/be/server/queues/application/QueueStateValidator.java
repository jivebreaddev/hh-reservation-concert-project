package kr.hhplus.be.server.queues.application;

import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class QueueStateValidator {
  private final StateMachine<QueueStatus, QueueEvent> stateMachine;

  public QueueStateValidator(StateMachine<QueueStatus, QueueEvent> stateMachine) {
    this.stateMachine = stateMachine;
  }

  public boolean isValidTransition(QueueStatus currentStatus, QueueEvent event) {
    stateMachine.getStateMachineAccessor().doWithAllRegions(accessor ->
        accessor.resetStateMachine(
            new DefaultStateMachineContext<>(currentStatus, null, null, null)
        )
    );

    stateMachine.start();

    return stateMachine.getTransitions().stream()
        .anyMatch(t -> t.getSource().getId() == currentStatus &&
            t.getTrigger().getEvent() == event);
  }
}
