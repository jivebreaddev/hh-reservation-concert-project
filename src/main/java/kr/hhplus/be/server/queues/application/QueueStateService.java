package kr.hhplus.be.server.queues.application;

import static kr.hhplus.be.server.queues.domain.event.QueueEvent.COMPLETE;
import static kr.hhplus.be.server.queues.domain.event.QueueEvent.PROCESS;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.springframework.stereotype.Service;

@Service
public class QueueStateService {

  private final QueueStateValidator queueStateValidator;
  private final QueueRepository repository;

  public QueueStateService(QueueStateValidator queueStateValidator, QueueRepository repository) {
    this.queueStateValidator = queueStateValidator;
    this.repository = repository;
  }

  public void handleProcessingEvent(QueueProcessingEvent event) {
    List<Queue> queues = repository.findAllByCreatedAtAsc();
    repository.removeFromWaitQueue(queues.stream()
        .map(q -> q.getId().toString())
        .collect(Collectors.toSet()));

    List<Queue> validQueues = queues.stream()
        .filter(queue -> queueStateValidator.isValidTransition(queue.getQueueStatus(), event.getQueueEvent()))
        .map(queue -> {
          switch (event.getQueueEvent()) {
            case PROCESS -> queue.toProcessing();
            default -> throw new UnsupportedOperationException("Unknown event");
          }

          return queue;
        })
            .toList();

    repository.saveAll(validQueues);
  }

  public void handleCompletedEvent(QueueCompletedEvent event) {
    List<Queue> queues = repository.findAllByUserId(event.getExpiringUserIds());

    List<Queue> validQueues = queues.stream()
        .filter(queue -> queueStateValidator.isValidTransition(queue.getQueueStatus(), event.getQueueEvent()))
        .map(queue -> {
          switch (event.getQueueEvent()) {
            case COMPLETE -> queue.toCompleted();
            default -> throw new UnsupportedOperationException("Unknown event");
          }
          return queue;
        })
        .toList();

    repository.removeFromQueue(
        validQueues.stream().map(q -> q.getId().toString()).collect(Collectors.toSet()));
  }
}
