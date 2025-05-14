package kr.hhplus.be.server.queues.infra;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultQueueRepository extends JpaRepository<Queue, UUID> {
  List<Queue> findAllByQueueStatus(QueueStatus queueStatus);

  List<Queue> findByQueueStatusOrderByCreatedAtAsc(QueueStatus queueStatus);

  Long countByQueueStatus(QueueStatus queueStatus);

  Optional<Queue> findByUserId(UUID userId);


  List<Queue> findAllByUserId(UUID userId);
}
