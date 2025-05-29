package kr.hhplus.be.server.queues.ui.scheduler;

import kr.hhplus.be.server.common.annotation.Scheduler;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

@Scheduler
public class EnqueueScheduler {

  private final ApplicationEventPublisher applicationEventPublisher;

  public EnqueueScheduler(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }


  @Scheduled(fixedDelay = 3000)
  private void processQueue() {
    applicationEventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));

  }
}
