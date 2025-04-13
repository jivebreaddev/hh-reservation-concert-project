package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.QueueStatus;

@Schema
public class QueueResponse {

  private final UUID userId;
  private final QueueStatus queueStatus;
  private final UUID token;

  public QueueResponse(UUID userId, QueueStatus queueStatus, UUID token) {
    this.userId = userId;
    this.queueStatus = queueStatus;
    this.token = token;
  }

  public UUID getUserId() {
    return userId;
  }

  public QueueStatus getQueueStatus() {
    return queueStatus;
  }

  public UUID getToken() {
    return token;
  }
}
