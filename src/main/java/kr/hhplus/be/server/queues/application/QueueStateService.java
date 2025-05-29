package kr.hhplus.be.server.queues.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class QueueStateService {

  private final QueueStateValidator queueStateValidator;
  private final QueueRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  public QueueStateService(QueueStateValidator queueStateValidator, QueueRepository repository,
      ApplicationEventPublisher eventPublisher) {
    this.queueStateValidator = queueStateValidator;
    this.repository = repository;
    this.eventPublisher = eventPublisher;
  }


  public void handleProcessingEvent(QueueProcessingEvent event) {
    List<Queue> queues = repository.findAllByCreatedAtAsc();

    Set<String> ids = queues.stream()
        .map(q -> q.getUserId().toString())
        .collect(Collectors.toSet());
    repository.removeFromWaitQueue(ids);

    List<Queue> validQueues = queues.stream()
        .filter(queue -> queueStateValidator.isValidTransition(queue.getQueueStatus(),
            event.getQueueEvent()))
        .map(queue -> {
          switch (event.getQueueEvent()) {
            case PROCESS -> queue.toProcessing();
            default -> throw new UnsupportedOperationException("Unknown event");
          }

          return queue;
        })
        .toList();

    repository.saveAll(validQueues);
    repository.toActiveToken(validQueues);

    eventPublisher.publishEvent(new QueueCompletedEvent(QueueEvent.COMPLETE, queues.stream()
        .map(Queue::getId)
        .toList()));
  }

  public void handleCompletedEvent(QueueCompletedEvent event) {
    List<Queue> queues = repository.findAllByUserId(event.getExpiringUserIds());

    List<Queue> validQueues = queues.stream()
        .filter(queue -> queueStateValidator.isValidTransition(queue.getQueueStatus(),
            event.getQueueEvent()))
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
