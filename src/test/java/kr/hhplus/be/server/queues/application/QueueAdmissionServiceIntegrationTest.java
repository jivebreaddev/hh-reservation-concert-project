package kr.hhplus.be.server.queues.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueueAdmissionServiceIntegrationTest extends IntegrationTest {
  @Autowired
  QueueRepository queueRepository;

  @Autowired
  QueueAdmissionService admissionService;

  @Autowired
  DatabaseCleanup databaseCleanup;

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of("queues"));
  }

  @Test
  @DisplayName("WAITING 상태 큐가 처리 제한 개수 이하일 경우, 모두 PROCESSING으로 변경")
  void processWaitingToProcessing() {
    // given
    for (int i = 0; i < 2; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }

    // when
    admissionService.processQueue();

    // then
    List<Queue> all = queueRepository.findByQueueStatus(QueueStatus.PROCESSING);
    assertThat(all).allMatch(q -> q.getQueueStatus() == QueueStatus.PROCESSING);
  }

  @Test
  @DisplayName("기존에 PROCESSING 상태가 일부 존재하면 남은 처리 개수만큼만 상태 변경")
  void processQueueRemainingStatusChange() {
    // given
    queueRepository.save(Queue.of(UUID.randomUUID()));
    queueRepository.save(Queue.of(UUID.randomUUID()));

    // WAITING 3개
    for (int i = 0; i < 3; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }

    // when
    admissionService.processQueue();

    // then
    long processingCount = queueRepository.countByQueueStatus(QueueStatus.PROCESSING);
    assertThat(processingCount).isEqualTo(5);
  }

  @Test
  @DisplayName("이미 최대 처리 중이면 아무 것도 바뀌지 않음")
  void processQueueMaxDoNothing() {
    // given
    for (int i = 0; i < 3; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }
    for (int i = 0; i < 15; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }

    // when
    admissionService.processQueue();

    // then
    long stillWaiting = queueRepository.countByQueueStatus(QueueStatus.WAITING);
    assertThat(stillWaiting).isEqualTo(8);
  }
}
