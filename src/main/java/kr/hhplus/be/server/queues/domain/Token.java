package kr.hhplus.be.server.queues.domain;

import java.util.Objects;
import java.util.UUID;


public class Token {
  private UUID id;

  private UUID userId;

  private UUID queueId;


  protected Token(UUID userId, UUID queueId) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.queueId = queueId;
  }

  protected Token() {
  }

  public static Token of(UUID userId, UUID queueId) {
    return new Token(userId, queueId);
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

}
