package kr.hhplus.be.server.queues.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QueueTest {

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 큐_상태가_변경될_때 {

    @Test
    @DisplayName("대기 중인 큐를 처리 중 상태로 변경합니다.")
    void toProcessing() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      Queue queue = Queue.of(userId);

      // WHEN
      queue.toProcessing();

      // THEN
      assertThat(queue.getQueueStatus()).isEqualTo(QueueStatus.PROCESSING);
    }

    @Test
    @DisplayName("처리 중인 큐를 완료 상태로 변경합니다.")
    void toCompleted() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      Queue queue = Queue.of(userId).toProcessing(); // 처리 중 상태로 먼저 변경

      // WHEN
      queue.toCompleted();

      // THEN
      assertThat(queue.getQueueStatus()).isEqualTo(QueueStatus.COMPLETED);
    }
  }
  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 생성시_기본값_검증 {

    @Test
    @DisplayName("생성 시 createdAt과 updatedAt은 현재 시간과 거의 동일해야 합니다.")
    void createdAt_and_updatedAt_should_be_near_now() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      LocalDateTime before = LocalDateTime.now();

      // WHEN
      Queue queue = Queue.of(userId);

      // THEN
      LocalDateTime after = LocalDateTime.now();
      assertThat(queue.getQueueStatus()).isEqualTo(QueueStatus.WAITING);
      assertThat(queue.getCreatedAt()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
      assertThat(queue.getUpdatedAt()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
    }
  }
}
