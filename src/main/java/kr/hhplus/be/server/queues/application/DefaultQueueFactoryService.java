package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueuePosition;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.springframework.stereotype.Service;

@Service
public class DefaultQueueFactoryService implements QueueFactoryUseCase {

  private final QueueRepository queueRepository;

  protected DefaultQueueFactoryService(QueueRepository queueRepository) {
    this.queueRepository = queueRepository;
  }

  @Override
  public Queue createQueue(UUID userId) {
    return queueRepository.findByUserId(userId)
        .orElseGet(() -> queueRepository.save(Queue.of(userId)));
  }

  @Override
  public QueuePosition getQueueStatus(UUID userId) {
    return queueRepository.findQueueStatusByUserId(userId)
        .orElseGet(() -> QueuePosition.of(QueueStatus.WAITING, 0L));
  }
}
