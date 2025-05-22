package kr.hhplus.be.server.queues.domain;

public class QueuePosition {

  private final QueueStatus queueStatus;
  private final Long order;

  protected QueuePosition(QueueStatus queueStatus, Long order) {
    this.queueStatus = queueStatus;
    this.order = order;
  }

  public static QueuePosition of(QueueStatus queueStatus, Long order){
    return new QueuePosition(queueStatus, order);
  }

  public QueueStatus getQueueStatus() {
    return queueStatus;
  }

  public Long getOrder() {
    return order;
  }
}
