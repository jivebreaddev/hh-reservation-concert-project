package kr.hhplus.be.server.queues.application;

import java.util.List;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import org.springframework.stereotype.Service;

@Service
public class QueueStateService {

  private final QueueStateValidator queueStateValidator;
  private final QueueRepository repository;

  public QueueStateService(QueueStateValidator queueStateValidator, QueueRepository repository) {
    this.queueStateValidator = queueStateValidator;
    this.repository = repository;
  }

  public void handleEvent(QueueEvent event) {
    List<Queue> queues = repository.findAllByCreatedAtAsc();

    List<Queue> validQueues = queues.stream()
        .filter(queue -> queueStateValidator.isValidTransition(queue.getQueueStatus(), event))
        .map(queue -> {
          switch (event) {
            case PROCESS -> queue.toProcessing();
            case COMPLETE -> queue.toCompleted();
            default -> throw new UnsupportedOperationException("Unknown event");
          }

          return queue;
        })
            .toList();

    repository.saveAll(validQueues);
  }

}
