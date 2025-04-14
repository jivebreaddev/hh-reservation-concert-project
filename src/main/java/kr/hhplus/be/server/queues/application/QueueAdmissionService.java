package kr.hhplus.be.server.queues.application;

import java.util.List;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QueueAdmissionService implements QueueAdmissionUseCase {

  private final QueueRepository queueRepository;

  private static final Long ALLOWED_COUNTS = 10L;

  protected QueueAdmissionService(QueueRepository queueRepository) {
    this.queueRepository = queueRepository;
  }

  @Override
  @Transactional
  public void processQueue() {
    Long processed = queueRepository.countByQueueStatus(QueueStatus.PROCESSING);

    List<Queue> queues = queueRepository.findByQueueStatusOrderByCreatedAtAsc(QueueStatus.WAITING);

    List<Queue> processeingQueues = queues
        .stream()
        .limit(ALLOWED_COUNTS - processed)
        .map(Queue::toProcessing)
        .toList();

    queueRepository.saveAll(processeingQueues);
  }
}
