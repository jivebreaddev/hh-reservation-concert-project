package kr.hhplus.be.server.queues.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.springframework.stereotype.Repository;

@Repository
public class QueueRepositoryJpaImpl implements QueueRepository {
  private final DefaultQueueRepository defaultQueueRepository;

  protected QueueRepositoryJpaImpl(DefaultQueueRepository defaultQueueRepository) {
    this.defaultQueueRepository = defaultQueueRepository;
  }

  @Override
  public List<Queue> findByQueueStatus(QueueStatus queueStatus) {
    return defaultQueueRepository.findAllByQueueStatus(queueStatus);
  }

  @Override
  public void saveAll(List<Queue> processeingQueues) {
    defaultQueueRepository.saveAll(processeingQueues);
  }

  @Override
  public List<Queue> findByQueueStatusOrderByCreatedAtAsc(QueueStatus queueStatus) {
    return defaultQueueRepository.findByQueueStatusOrderByCreatedAtAsc(queueStatus);
  }

  @Override
  public Long countByQueueStatus(QueueStatus queueStatus) {
    return defaultQueueRepository.countByQueueStatus(queueStatus);
  }

  @Override
  public Queue save(Queue of) {
    return defaultQueueRepository.save(of);
  }

  @Override
  public Optional<Queue> findByUserId(UUID userId) {
    return defaultQueueRepository.findByUserId(userId);
  }
}
