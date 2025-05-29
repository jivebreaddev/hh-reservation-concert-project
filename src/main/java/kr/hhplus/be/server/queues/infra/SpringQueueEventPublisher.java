package kr.hhplus.be.server.queues.infra;

import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEventPublisher;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringQueueEventPublisher implements QueueEventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  public SpringQueueEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void publishQueueProcessingEvent(){
    applicationEventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));
  }

}
