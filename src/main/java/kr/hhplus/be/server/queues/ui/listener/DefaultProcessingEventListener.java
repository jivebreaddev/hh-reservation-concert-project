package kr.hhplus.be.server.queues.ui.listener;

import kr.hhplus.be.server.common.annotation.DomainEventListener;
import kr.hhplus.be.server.queues.application.QueueStateService;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@DomainEventListener
public class DefaultProcessingEventListener implements ProcessingEventListener {
  private final QueueStateService queueStateService;

  public DefaultProcessingEventListener(QueueStateService queueStateService) {
    this.queueStateService = queueStateService;
  }

  @EventListener
  public void handleProcessing(QueueProcessingEvent event) {
    queueStateService.handleProcessingEvent(event);
  }

  @Async
  @EventListener
  public void handleCompleted(QueueCompletedEvent event) {
    queueStateService.handleCompletedEvent(event);
  }
}
