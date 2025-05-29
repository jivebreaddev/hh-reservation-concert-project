package kr.hhplus.be.server.queues.ui.scheduler;

import kr.hhplus.be.server.common.annotation.Scheduler;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEventPublisher;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

@Scheduler
public class EnqueueScheduler {

  private final QueueEventPublisher queueEventPublisher;

  public EnqueueScheduler(QueueEventPublisher queueEventPublisher) {
    this.queueEventPublisher = queueEventPublisher;
  }


  @Scheduled(fixedDelay = 3000)
  private void processQueue() {
    queueEventPublisher.publishQueueProcessingEvent();

  }
}
