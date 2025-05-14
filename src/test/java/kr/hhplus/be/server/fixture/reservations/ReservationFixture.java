package kr.hhplus.be.server.fixture.reservations;

import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.Reservation;

public class ReservationFixture {

  public static Reservation create(UUID userId, UUID seatId, UUID concertId, Long expiresInMinutes) {
    return Reservation.createTemporaryReservation(UUID.randomUUID(), userId, seatId, concertId, expiresInMinutes);
  }

  public static Reservation createWithCustomId(UUID reservationId, UUID userId, UUID seatId, UUID concertId, Long expiresInMinutes) {
    return Reservation.createTemporaryReservation(reservationId, userId, seatId, concertId, expiresInMinutes);
  }
}
