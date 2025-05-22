package kr.hhplus.be.server.queues.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface QueueRepository {

  List<Queue> saveAll(List<Queue> processeingQueues);

  Queue save(Queue processeingQueues);

  List<Queue> findAllByCreatedAtAsc();

  Long countByQueueStatus();

  Optional<QueuePosition> findQueueStatusByUserId(UUID userId);
  Optional<Queue> findByUserId(UUID userId);

  void toActiveToken(List<Token> tokenIds);

  List<Queue> findAllByUserId(List<UUID> userIds);

  void removeFromWaitQueue(Set<String> keys);

  void removeFromQueue(Set<String> keys);

}
