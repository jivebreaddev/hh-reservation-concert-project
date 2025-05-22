package kr.hhplus.be.server.queues.domain.event;

public class QueueProcessingEvent {
  private final QueueEvent queueEvent;

  public QueueProcessingEvent(QueueEvent queueEvent) {
    this.queueEvent = queueEvent;
  }

  public QueueEvent getQueueEvent() {
    return queueEvent;
  }
}
