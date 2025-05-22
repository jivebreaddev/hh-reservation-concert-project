package kr.hhplus.be.server.queues.application;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import org.springframework.stereotype.Component;

@Component
public class QueueAccessVerifier {

  private final QueueRepository queueRepository;

  public QueueAccessVerifier(QueueRepository queueRepository) {
    this.queueRepository = queueRepository;
  }

  public boolean canAccess(String userId) {
    Queue queue = queueRepository.findByUserId(UUID.fromString(userId))
        .orElseThrow(() -> new RuntimeException());

    return queue.isProcessing();
  }
}
