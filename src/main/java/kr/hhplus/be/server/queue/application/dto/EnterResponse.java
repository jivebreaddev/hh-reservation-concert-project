package kr.hhplus.be.server.queue.application.dto;

import kr.hhplus.be.server.queue.domain.QueueStatus;

public class EnterResponse {
  private final Long userId;
  private final Long percentage;
  private final QueueStatus queueStatus;

  public EnterResponse(Long userId, Long percentage, QueueStatus queueStatus) {
    this.userId = userId;
    this.percentage = percentage;
    this.queueStatus = queueStatus;
  }
}
