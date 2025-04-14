package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class EnterRequest {

  private final UUID userId;

  public EnterRequest(UUID userId) {
    this.userId = userId;
  }

  public UUID getUserId() {
    return userId;
  }
}
