package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.QueueStatus;

@Schema
public class EnterResponse {

  private final UUID userId;
  private final QueueStatus queueStatus;

  public EnterResponse(UUID userId, QueueStatus queueStatus) {
    this.userId = userId;
    this.queueStatus = queueStatus;
  }

  public UUID getUserId() {
    return userId;
  }


  public QueueStatus getQueueStatus() {
    return queueStatus;
  }
}
