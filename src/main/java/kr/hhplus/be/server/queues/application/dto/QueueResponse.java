package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.queues.domain.QueueStatus;
@Schema
public class QueueResponse {
  private final Long userId;
  private final QueueStatus queueStatus;

  public QueueResponse(Long userId, QueueStatus queueStatus) {
    this.userId = userId;
    this.queueStatus = queueStatus;
  }

  public Long getUserId() {
    return userId;
  }

  public QueueStatus getQueueStatus() {
    return queueStatus;
  }
}
