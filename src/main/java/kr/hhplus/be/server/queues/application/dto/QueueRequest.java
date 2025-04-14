package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class QueueRequest {

  private final UUID userId;

  public QueueRequest(UUID userId) {
    this.userId = userId;
  }

  public UUID getUserId() {
    return userId;
  }
}
