package kr.hhplus.be.server.queues.domain.event;

import java.util.List;
import java.util.UUID;

public class QueueCompletedEvent {
  private final QueueEvent queueEvent;
  private final List<UUID> expiringUserIds;

  public QueueCompletedEvent(QueueEvent queueEvent, List<UUID> expiringUserIds) {
    this.queueEvent = queueEvent;
    this.expiringUserIds = expiringUserIds;
  }

  public QueueEvent getQueueEvent() {
    return queueEvent;
  }

  public List<UUID> getExpiringUserIds() {
    return expiringUserIds;
  }
}
