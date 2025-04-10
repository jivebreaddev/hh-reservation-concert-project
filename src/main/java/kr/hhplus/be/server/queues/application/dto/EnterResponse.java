package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.queues.domain.QueueStatus;
@Schema
public class EnterResponse {
  private final Long userId;
  private final Long percentage;
  private final QueueStatus queueStatus;

  public EnterResponse(Long userId, Long percentage, QueueStatus queueStatus) {
    this.userId = userId;
    this.percentage = percentage;
    this.queueStatus = queueStatus;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getPercentage() {
    return percentage;
  }

  public QueueStatus getQueueStatus() {
    return queueStatus;
  }
}
