package kr.hhplus.be.server.queues.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueRepository {

  List<Queue> findByQueueStatus(QueueStatus queueStatus);

  void saveAll(List<Queue> processeingQueues);

  List<Queue> findByQueueStatusOrderByCreatedAtAsc(QueueStatus queueStatus);

  Long countByQueueStatus(QueueStatus queueStatus);

  Queue save(Queue of);

  Optional<Queue> findByUserId(UUID userId);
}
