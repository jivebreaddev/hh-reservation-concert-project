package kr.hhplus.be.server.queues.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.QueueStatus;

@Schema
public class EnterResponse {

  private UUID userId;
  private QueueStatus queueStatus;
  private Long queuePosition;

  public EnterResponse() {
  }

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

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setQueueStatus(QueueStatus queueStatus) {
    this.queueStatus = queueStatus;
  }

  public void setQueuePosition(Long queuePosition) {
    this.queuePosition = queuePosition;
  }
}
