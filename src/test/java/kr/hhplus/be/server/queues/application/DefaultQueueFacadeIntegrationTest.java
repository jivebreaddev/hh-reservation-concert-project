package kr.hhplus.be.server.queues.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.payments.infra.DefaultPaymentRepository;
import kr.hhplus.be.server.queues.application.dto.EnterRequest;
import kr.hhplus.be.server.queues.application.dto.EnterResponse;
import kr.hhplus.be.server.queues.application.dto.QueueRequest;
import kr.hhplus.be.server.queues.application.dto.QueueResponse;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultQueueFacadeIntegrationTest extends IntegrationTest {
  @Autowired
  QueueFacade queueService;
  @Autowired
  QueueRepository queueRepository;
  @Autowired
  TokenRepository tokenRepository;
  @Autowired
  DatabaseCleanup databaseCleanup;

  @Autowired
  DefaultPaymentRepository repository;

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of("queues"));
  }

  @Test
  @DisplayName("큐가 없을 때 queueUser 호출하면 새 Queue와 Token을 생성한다")
  void queueUserWithQueueAndToken() {
    UUID userId = UUID.randomUUID();
    QueueRequest request = new QueueRequest(userId);

    QueueResponse response = queueService.queueUser(request);

    assertThat(response.getUserId()).isEqualTo(userId);
    assertThat(queueRepository.findByUserId(userId)).isPresent();
    assertThat(tokenRepository.findById(response.getToken())).isPresent();
  }

  @Test
  @DisplayName("getQueueStatus 호출 시 해당 유저의 Queue 상태를 반환한다")
  void getQueueStatus() {
    UUID userId = UUID.randomUUID();
    Queue queue = queueRepository.save(Queue.of(userId));

    EnterResponse response = queueService.getQueueStatus(new EnterRequest(userId));

    assertThat(response.getUserId()).isEqualTo(userId);
    assertThat(response.getQueueStatus()).isEqualTo(queue.getQueueStatus());
  }

  @Test
  @DisplayName("getQueueStatus 호출 시 Queue가 없으면 예외 발생")
  void getQueueStatusWhenQueueNotExists() {
    UUID userId = UUID.randomUUID();

    assertThatThrownBy(() -> queueService.getQueueStatus(new EnterRequest(userId)))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("동일 유저 중복 진입 시 하나만 성공")
  void concurrentQueueDuplicateEnterTest() throws InterruptedException {
    int numberOfThreads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    UUID userId = UUID.randomUUID();

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          queueService.queueUser(new QueueRequest(userId));
        } catch (Exception e) {

        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    // 해당 유저에 대한 대기열은 하나만 있어야 함
    List<Queue> queues = queueRepository.findAllByUserId(userId);
    assertThat(queues).hasSize(1);
  }
}
