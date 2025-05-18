package kr.hhplus.be.server.queues.ui.listener;

import kr.hhplus.be.server.common.annotation.DomainEventListener;
import kr.hhplus.be.server.queues.application.QueueStateService;
import kr.hhplus.be.server.queues.event.QueueEvent;
import org.springframework.context.event.EventListener;

@DomainEventListener
public class DefaultConcertEventListener implements ConcertEventListener {
  private final QueueStateService queueStateService;

  public DefaultConcertEventListener(QueueStateService queueStateService) {
    this.queueStateService = queueStateService;
  }

  @EventListener
  public void handleReserved(QueueEvent event) {
    queueStateService.handleEvent(event);
  }

}
