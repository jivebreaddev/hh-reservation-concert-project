package kr.hhplus.be.server.queues.domain.event;

public interface QueueEventPublisher {
  void publishQueueProcessingEvent();
}
