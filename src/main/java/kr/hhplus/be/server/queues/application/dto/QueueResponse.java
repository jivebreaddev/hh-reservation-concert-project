package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class QueueResponse {

  private UUID userId;
  private UUID token;

  public QueueResponse() {
  }

  public QueueResponse(UUID userId, UUID token) {
    this.userId = userId;
    this.token = token;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getToken() {
    return token;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setToken(UUID token) {
    this.token = token;
  }
}
