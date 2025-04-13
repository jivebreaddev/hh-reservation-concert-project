package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class QueueRequest {
  private final Long userId;

  public QueueRequest(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
