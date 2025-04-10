package kr.hhplus.be.server.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MoneyTest {

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 포인트_계산을_하는_경우 {

    @ParameterizedTest
    @CsvSource(value = {"1000,1000", "0,0", "8550,1450", "8000,2000", "1,1"})
    @DisplayName("[성공] Money 끼리 합칠시에 더한 값이 반환됩니다.")
    void add(Long from, Long to) {
      // GIVEN
      Money original = Money.of(from);
      Money add = Money.of(to);
      // WHEN
      Money result = original.add(add);

      // THEN
      assertThat(result).isEqualTo(Money.of(from + to));
    }

    @ParameterizedTest
    @CsvSource(value = {"1000,1000", "0,0", "8550,1450", "6000,2000", "1,1"})
    @DisplayName("[성공] Money 끼리 합칠시에 더한 값이 반환됩니다.")
    void subtract(Long from, Long to) {
      // GIVEN
      Money original = Money.of(from);
      Money add = Money.of(to);
      // WHEN
      Money result = original.subtract(add);

      // THEN
      assertThat(result).isEqualTo(Money.of(from - to));
    }

    @Test
    @DisplayName("[실패] amount가 빈값일 경우 예외를 표기한다.")
    void getUserMoneyWithEmptyInput() {
      // GIVEN
      Long emptyMoney = null;

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> Money.of(emptyMoney));
    }

    @Test
    @DisplayName("[실패] amount가 음수일 경우 예외를 표기한다.")
    void getUserMoneyWithNegativeInput() {
      // GIVEN
      Long emptyMoney = -1000L;

      // THEN
      assertThrows(IllegalArgumentException.class,
          () -> Money.of(emptyMoney));
    }
  }
}
