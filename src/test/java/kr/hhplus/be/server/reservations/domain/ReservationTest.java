package kr.hhplus.be.server.reservations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ReservationTest {

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 콘서트_예약_상태가_변경될_때 {

    @DisplayName("좌석에 대한 임시 예약시에 임시 상태로 변경합니다.")
    @ParameterizedTest
    @EnumSource(ReservationStatus.class)
    void createTemporaryReservation(ReservationStatus status) {
      // GIVEN
      UUID id = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID seatId = UUID.randomUUID();
      UUID concertId = UUID.randomUUID();

      // WHEN
      Reservation original = Reservation.createTemporaryReservation(id, userId, seatId, concertId, 5L);

      // THEN
      assertThat(original.getReservationStatus()).isEqualTo(ReservationStatus.PENDING);

    }

    @DisplayName("좌석에 대한 예약시에 예약 상태로 변경합니다.")
    @Test
    void createReservation() {
      // GIVEN
      UUID id = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID seatId = UUID.randomUUID();
      UUID concertId = UUID.randomUUID();

      // WHEN
      Reservation original = Reservation.createTemporaryReservation(id, userId, seatId, concertId,
          5L);
      original.createReservation();
      // THEN
      assertThat(original.getReservedAt()).isNotNull();
      assertThat(original.getReservationStatus()).isEqualTo(ReservationStatus.CONFIRMED);

    }


  }

}
