package kr.hhplus.be.server.queues.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TokenTest {
  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 토큰_생성시 {

    @Test
    @DisplayName("createdAt은 현재 시간과 거의 동일하게 설정되고 expiredAt은 null입니다.")
    void createdAt_is_now_and_expiredAt_is_null() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      UUID queueId = UUID.randomUUID();
      LocalDateTime before = LocalDateTime.now();

      // WHEN
      Token token = Token.of(userId, queueId);

      // THEN
      LocalDateTime after = LocalDateTime.now();
      assertThat(token.getCreatedAt()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
      assertThat(token.getExpiredAt()).isNull();
    }
  }

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 만료시간_설정시 {

    @Test
    @DisplayName("expireAfter를 호출하면 expiredAt이 설정됩니다.")
    void expireAfter_sets_expiredAt_properly() {
      // GIVEN
      UUID userId = UUID.randomUUID();
      UUID queueId = UUID.randomUUID();
      Token token = Token.of(userId, queueId);
      Duration duration = Duration.ofMinutes(10);
      LocalDateTime before = LocalDateTime.now();
      // WHEN
      token.expireAfter(duration);

      // THEN
      LocalDateTime after = LocalDateTime.now();
      assertThat(token.getExpiredAt()).isBetween(before.minusMinutes(1), after.plusMinutes(11));

    }
  }
}
