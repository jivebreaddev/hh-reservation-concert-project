package kr.hhplus.be.server.queues.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class QueueStateServiceTest {
  @Mock
  QueueStateValidator queueStateValidator;

  @Mock
  QueueRepository repository;

  @Mock
  ApplicationEventPublisher eventPublisher;

  @InjectMocks
  QueueStateService service;

  @Test
  @DisplayName("QueueStateService - 처리 이벤트 처리 및 이벤트 발행 테스트")
  void handleProcessAndPublishEvent() {
    // given
    Queue queue1 = mock(Queue.class);
    Queue queue2 = mock(Queue.class);

    List<Queue> queues = List.of(queue1, queue2);
    when(repository.findAllByCreatedAtAsc()).thenReturn(queues);

    when(queue1.getUserId()).thenReturn(UUID.randomUUID());
    when(queue2.getUserId()).thenReturn(UUID.randomUUID());

    when(queue1.getQueueStatus()).thenReturn(null);
    when(queue2.getQueueStatus()).thenReturn(null);

    when(queueStateValidator.isValidTransition(any(), any())).thenReturn(true);

    QueueProcessingEvent event = new QueueProcessingEvent(QueueEvent.PROCESS);

    // when
    service.handleProcessingEvent(event);

    // then
    verify(repository).removeFromWaitQueue(anySet());
    verify(queue1).toProcessing();
    verify(queue2).toProcessing();
    verify(repository).saveAll(queues);

    ArgumentCaptor<QueueCompletedEvent> captor = ArgumentCaptor.forClass(QueueCompletedEvent.class);
    verify(eventPublisher).publishEvent(captor.capture());

    QueueCompletedEvent publishedEvent = captor.getValue();
    assertEquals(QueueEvent.COMPLETE, publishedEvent.getQueueEvent());
    Set<UUID> ids = queues.stream().map(Queue::getId).collect(Collectors.toSet());
    assertTrue(publishedEvent.getExpiringUserIds().containsAll(ids));
  }

  @Test
  @DisplayName("QueueStateService - 완료 이벤트 처리 및 큐 제거 테스트")
  void handleCompletedEvent_shouldCompleteAndRemoveQueues() {
    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    Queue queue1 = mock(Queue.class);
    Queue queue2 = mock(Queue.class);

    when(queue1.getId()).thenReturn(id1);
    when(queue2.getId()).thenReturn(id2);

    List<Queue> queues = List.of(queue1, queue2);

    when(repository.findAllByUserId(any())).thenReturn(queues);
    when(queue1.getQueueStatus()).thenReturn(null);
    when(queue2.getQueueStatus()).thenReturn(null);

    when(queueStateValidator.isValidTransition(any(), any())).thenReturn(true);

    QueueCompletedEvent event = new QueueCompletedEvent(QueueEvent.COMPLETE, queues.stream().map(Queue::getId).toList());

    // when
    service.handleCompletedEvent(event);

    // then
    verify(queue1).toCompleted();
    verify(queue2).toCompleted();

    ArgumentCaptor<Set<String>> captor = ArgumentCaptor.forClass(Set.class);
    verify(repository).removeFromQueue(captor.capture());

    Set<String> removedIds = captor.getValue();
    assertTrue(removedIds.contains(id1.toString()));
    assertTrue(removedIds.contains(id2.toString()));
  }

}
