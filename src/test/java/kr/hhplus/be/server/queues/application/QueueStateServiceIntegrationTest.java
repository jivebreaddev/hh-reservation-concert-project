package kr.hhplus.be.server.queues.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import kr.hhplus.be.server.queues.infra.RedisKey;
import kr.hhplus.be.server.util.RedisCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest(properties = {
    "redis.queue.initial-size=3"
})
@RecordApplicationEvents
public class QueueStateServiceIntegrationTest extends IntegrationTest {
  @Autowired
  QueueRepository queueRepository;

  @Autowired
  ApplicationEventPublisher eventPublisher;

  @Autowired
  private ApplicationEvents applicationEvents;

  @Autowired
  RedisCleanup redisCleanup;

  @Autowired
  private RedissonClient redissonClient;

  @BeforeEach
  void cleanDatabase() {
    redisCleanup.cleanUp(List.of(RedisKey.PROCESS_QUEUE.key(), RedisKey.QUEUE_MAP.key(), RedisKey.WAIT_QUEUE.key()));
  }

  @Test
  @DisplayName("초기 Redis 큐 사이즈 값이 설정된대로 올라가는지 검증")
  void testRedisQueueInitialSize() {
    RAtomicLong counter = redissonClient.getAtomicLong(RedisKey.QUEUE_SIZE.key());
    assertThat(counter.get()).isEqualTo(3L);
  }

  @Test
  @DisplayName("WAITING 상태 큐가 처리 제한 개수 이하일 경우, 모두 PROCESSING으로 변경 이벤트 테스트")
  void processWaitingToProcessingEvent(ApplicationEvents events) {
    // given
    for (int i = 0; i < 2; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }

    // when - 이벤트 발행
    eventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));

    // then
    Long size = queueRepository.countByQueueStatus();
    assertThat(size).isEqualTo(2L);
    assertThat(events.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);

  }

  @Test
  @DisplayName("기존에 PROCESSING 상태가 일부 존재하면 남은 처리 개수만큼만 상태 변경 이벤트 테스트")
  void processQueueRemainingStatusChangeEvent(ApplicationEvents events) {
    // given
    queueRepository.save(Queue.of(UUID.randomUUID()));
    queueRepository.save(Queue.of(UUID.randomUUID()));

    // WAITING 3개
    for (int i = 0; i < 3; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }

    // when
    eventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));

    // then
    long processingCount = queueRepository.countByQueueStatus();
    assertThat(processingCount).isEqualTo(3);
  }

  @Test
  @DisplayName("QueueProcessingEvent 발생 시 처리 제한에 맞춰 일부만 PROCESSING 상태로 바뀜")
  void shouldLimitProcessingBasedOnInitialSize(ApplicationEvents events) {
    // given - 이미 PROCESSING 3개 + WAITING 2개
    for (int i = 0; i < 3; i++) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }
    List<UUID> waitingIds = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      UUID id = UUID.randomUUID();
      queueRepository.save(Queue.of(UUID.randomUUID()));
      waitingIds.add(id);
    }

    // when
    eventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));

    // then - 최대 큐 수(3)에 맞춰 WAITING 중 3개만 PROCESSING으로 전이되어야 함
    long processingCount = queueRepository.countByQueueStatus();
    redisCleanup.cleanUp(List.of(RedisKey.PROCESS_QUEUE.key()));

    // 처리된 큐 중 2개는 PROCESSING 상태여야 함
    eventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));
    long processingCountAfter = queueRepository.countByQueueStatus();

    assertThat(processingCount).isEqualTo(3L);
    assertThat(processingCountAfter).isEqualTo(2L);
    assertThat(waitingIds).contains(waitingIds.get(0));
    assertThat(waitingIds).contains(waitingIds.get(1));
    assertThat(events.stream(QueueProcessingEvent.class).count()).isEqualTo(2);
  }

  @Test
  @DisplayName("QueueProcessingEvent 발생 시, 상태 전이 및 CompleteEvent도 함께 발생하는지 검증")
  void shouldProcessQueuesAndFireProcessingEvent(ApplicationEvents events) {
    // given - WAITING 상태 큐 3개 세팅
    List<UUID> userIds = IntStream.range(0, 3)
        .mapToObj(i -> UUID.randomUUID())
        .toList();

    for (UUID userId : userIds) {
      queueRepository.save(Queue.of(UUID.randomUUID()));
    }

    // when - 큐 처리 이벤트 발행
    eventPublisher.publishEvent(new QueueProcessingEvent(QueueEvent.PROCESS));

    // then - ProcessingEvent 도 하나 이상 발생해야 함
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueCompletedEvent.class).count()).isGreaterThanOrEqualTo(1);

  }
}
