package kr.hhplus.be.server.queues.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "queues")
public class Queue {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;

  @Column(name = "user_id", columnDefinition = "binary(16)")
  private UUID userId;

  @Column(name = "status", nullable = false, columnDefinition = "varchar(255)")
  @Enumerated(EnumType.STRING)
  private QueueStatus queueStatus;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
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

  public QueueStatus getQueueStatus() {
    return queueStatus;
  }

  public UUID getId() {
    return id;
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

  public Queue toProcessing() {
    this.queueStatus = QueueStatus.PROCESSING;
    return this;
  }

  public Queue toCompleted() {
    this.queueStatus = QueueStatus.COMPLETED;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
