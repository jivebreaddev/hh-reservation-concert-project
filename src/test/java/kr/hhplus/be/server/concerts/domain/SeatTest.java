package kr.hhplus.be.server.concerts.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class SeatTest {

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 예약_상태가_변경될_때 {

    @DisplayName("예약에서 취소 상태일때 좌석은 예약가능 상태로 변경됩니다.")
    @ParameterizedTest
    @EnumSource(SeatStatus.class)
    void toAvailable(SeatStatus status) {
      // GIVEN
      UUID id = UUID.randomUUID();
      UUID concertUuid = UUID.randomUUID();
      Seat original = Seat.of(id, concertUuid, status, LocalDateTime.now());

      // WHEN
      original.toAvailable();

      // THEN
      assertThat(original.isAvailable()).isEqualTo(true);

    }

    @DisplayName("다른 상태에서 예약 불가 상태로 변경됩니다.")
    @ParameterizedTest
    @EnumSource(SeatStatus.class)
    void toHeld(SeatStatus status) {
      // GIVEN
      UUID id = UUID.randomUUID();
      UUID concertUuid = UUID.randomUUID();
      Seat original = Seat.of(id, concertUuid, status, LocalDateTime.now());

      // WHEN
      original.toHeld();

      // THEN
      assertThat(original.isHeld()).isEqualTo(true);

    }

    @DisplayName("다른 상태에서 예약 완료 상태로 변경됩니다.")
    @ParameterizedTest
    @EnumSource(SeatStatus.class)
    void toReserved(SeatStatus status) {
      // GIVEN
      UUID id = UUID.randomUUID();
      UUID concertUuid = UUID.randomUUID();
      Seat original = Seat.of(id, concertUuid, status, LocalDateTime.now());

      // WHEN
      original.toReserved();

      // THEN
      assertThat(original.isReserved()).isEqualTo(true);

    }


  }

}
