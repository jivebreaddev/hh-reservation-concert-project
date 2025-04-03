package kr.hhplus.be.server.queue.application.dto;

import kr.hhplus.be.server.queue.domain.QueueStatus;

public class QueueResponse {
  private final Long userId;
  private final QueueStatus queueStatus;

  public QueueResponse(Long userId, QueueStatus queueStatus) {
    this.userId = userId;
    this.queueStatus = queueStatus;
  }
}
