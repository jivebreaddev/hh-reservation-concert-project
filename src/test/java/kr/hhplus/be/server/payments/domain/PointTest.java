package kr.hhplus.be.server.payments.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.common.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PointTest {

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 포인트_계산을_하는_경우 {

    @ParameterizedTest
    @CsvSource(value = {"1000,1000", "0,0", "8550,1450", "8000,2000", "1,1"})
    @DisplayName("[성공] Point 끼리 합칠시에 더한 값이 반환됩니다.")
    void addPoint(Long from, Long to) {
      // GIVEN
      UUID userUuid = UUID.randomUUID();
      LocalDateTime current = LocalDateTime.now();
      Point original = Point.of(userUuid, from);

      // WHEN
      original.chargePoint(Money.of(to), current);

      // THEN
      assertThat(original.getBalance()).isEqualTo(Money.of(from + to).getAmount());
      assertThat(original.getUpdatedAt()).isEqualTo(current);

    }

    @ParameterizedTest
    @CsvSource(value = {"1000,1000", "0,0", "8550,1450", "6000,2000", "1,1"})
    @DisplayName("[성공] Point 끼리 합칠시에 더한 값이 반환됩니다.")
    void subtractPoint(Long from, Long to) {
      // GIVEN
      UUID userUuid = UUID.randomUUID();
      LocalDateTime current = LocalDateTime.now();
      Point original = Point.of(userUuid, from);
      // WHEN
      original.usePoint(Money.of(to), current);

      // THEN
      assertThat(original.getBalance()).isEqualTo(Money.of(from - to).getAmount());
      assertThat(original.getUpdatedAt()).isEqualTo(current);
    }

    @Test
    @DisplayName("[실패] amount가 빈값일 경우 예외를 표기한다.")
    void getUserPointWithEmptyInput() {
      // GIVEN
      UUID userUuid = UUID.randomUUID();
      Long emptyPoint = null;

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> Point.of(userUuid, emptyPoint));
    }

    @Test
    @DisplayName("[실패] amount가 음수일 경우 예외를 표기한다.")
    void getUserPointWithNegativeInput() {
      // GIVEN
      UUID userUuid = UUID.randomUUID();
      Long emptyPoint = -1000L;

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> Point.of(userUuid, emptyPoint));
    }
  }
}
