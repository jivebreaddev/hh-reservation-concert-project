package kr.hhplus.be.server.queues.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

class QueueAdmissionServiceTest {
  @Mock
  private QueueRepository queueRepository;

  @InjectMocks
  private QueueAdmissionService queueService;

  private static final Long ALLOWED_COUNTS = 10L;
  @Test
  @DisplayName("큐 상태가 WAITING인 큐들이 처리되어 PROCESSING 상태로 변경됩니다.")
  void processQueuesAndUpdateStatus() {
    // GIVEN
    Queue queue1 = Queue.of(UUID.randomUUID());
    Queue queue2 = Queue.of(UUID.randomUUID());
    queue1.toProcessing(); // 초기 상태가 PROCESSING인 큐
    queue2.toProcessing(); // 초기 상태가 PROCESSING인 큐

    List<Queue> waitingQueues = List.of(
        Queue.of(UUID.randomUUID()),
        Queue.of(UUID.randomUUID()) // 상태가 WAITING인 큐
    );

    when(queueRepository.countByQueueStatus(QueueStatus.PROCESSING)).thenReturn(2L);
    when(queueRepository.findByQueueStatusOrderByCreatedAtAsc(QueueStatus.WAITING)).thenReturn(waitingQueues);

    // WHEN
    queueService.processQueue();

    // THEN
    // 상태가 WAITING인 큐들 중 2 개의 큐가 PROCESSING으로 상태 변경
    List<Queue> expectedProcessedQueues = waitingQueues.subList(0, 2);

    verify(queueRepository, times(1)).saveAll(expectedProcessedQueues); // saveAll이 정확히 호출됐는지 확인
    assertThat(expectedProcessedQueues).allMatch(queue -> queue.getQueueStatus() == QueueStatus.PROCESSING); // 상태가 PROCESSING으로 변경되었는지 확인
  }

  @Test
  @DisplayName("처리할 수 있는 큐 개수가 최대에 도달하면 추가 큐는 처리되지 않습니다.")
  void processIfMaxCountReached() {
    // GIVEN
    Queue queue1 = Queue.of(UUID.randomUUID());
    queue1.toProcessing();
    List<Queue> waitingQueues = List.of(Queue.of(UUID.randomUUID()));

    when(queueRepository.countByQueueStatus(QueueStatus.PROCESSING)).thenReturn(ALLOWED_COUNTS);
    when(queueRepository.findByQueueStatusOrderByCreatedAtAsc(QueueStatus.WAITING)).thenReturn(waitingQueues);

    // WHEN
    queueService.processQueue();

    // THEN
    ArgumentCaptor<List<Queue>> captor = ArgumentCaptor.forClass(List.class);
    verify(queueRepository, atMostOnce()).saveAll(captor.capture());

    assertThat(captor.getValue()).isEmpty();
  }
}
