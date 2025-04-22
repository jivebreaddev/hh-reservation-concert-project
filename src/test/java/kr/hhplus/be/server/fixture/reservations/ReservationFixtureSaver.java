package kr.hhplus.be.server.fixture.reservations;

import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.Reservation;
import kr.hhplus.be.server.reservations.domain.ReservationRepository;
import org.springframework.stereotype.Component;

@Component
public class ReservationFixtureSaver {
  private final ReservationRepository reservationRepository;

  public ReservationFixtureSaver(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public Reservation saveTemporary(UUID userId, UUID seatId, Long expiresInMinutes) {
    Reservation reservation = ReservationFixture.create(userId, seatId, expiresInMinutes);
    return reservationRepository.save(reservation);
  }

  public Reservation saveConfirmed(UUID userId, UUID seatId) {
    Reservation reservation = ReservationFixture.create(userId, seatId, 10L);
    reservation.createReservation(); // CONFIRMED 상태로
    return reservationRepository.save(reservation);
  }
}
