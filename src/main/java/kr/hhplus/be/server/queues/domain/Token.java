package kr.hhplus.be.server.queues.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class Token {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;

  @Column(name = "user_id", columnDefinition = "binary(16)")
  private UUID userId;

  @Column(name = "queue_id", columnDefinition = "binary(16)")
  private UUID queueId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "expired_at", nullable = true)
  private LocalDateTime expiredAt;

  protected Token(UUID uuid, UUID userId, UUID queueId) {
    this.id = uuid;
    this.userId = userId;
    this.queueId = queueId;
    this.createdAt = LocalDateTime.now();
  }

  protected Token() {
  }

  public static Token of(UUID userId, UUID queueId) {
    return new Token(UUID.randomUUID(), userId, queueId);
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
    Token token = (Token) o;
    return Objects.equals(id, token.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getExpiredAt() {
    return expiredAt;
  }

  public void expireAfter(Duration duration) {
    this.expiredAt = LocalDateTime.now().plus(duration);
  }
}
