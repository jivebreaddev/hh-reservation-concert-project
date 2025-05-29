package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.QueueStatus;

@Schema
public class EnterResponse {

  private final UUID userId;
  private final QueueStatus queueStatus;
  private final Long queuePosition;

  public EnterResponse(UUID userId, QueueStatus queueStatus, Long queuePosition) {
    this.userId = userId;
    this.queueStatus = queueStatus;
    this.queuePosition = queuePosition;
  }

  public UUID getUserId() {
    return userId;
  }


  public QueueStatus getQueueStatus() {
    return queueStatus;
  }
}
