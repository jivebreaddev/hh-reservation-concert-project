package kr.hhplus.be.server.queues.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


public class Queue {

  private UUID id;

  private UUID userId;

  private QueueStatus queueStatus;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  protected Queue(UUID userId) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.queueStatus = QueueStatus.WAITING;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  protected Queue() {
  }

  public static Queue of(UUID userId) {
    return new Queue(userId);
  }

  public Queue toProcessing() {
    this.queueStatus = QueueStatus.PROCESSING;
    this.updatedAt = LocalDateTime.now();
    return this;
  }

  public Queue toCompleted() {
    this.queueStatus = QueueStatus.COMPLETED;
    this.updatedAt = LocalDateTime.now();
    return this;
  }

  public boolean isProcessing() {
    return this.queueStatus == QueueStatus.PROCESSING;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public QueueStatus getQueueStatus() {
    return queueStatus;
  }


  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Queue queue = (Queue) o;
    return Objects.equals(id, queue.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Queue{" +
        "id=" + id +
        ", userId=" + userId +
        ", queueStatus=" + queueStatus +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
