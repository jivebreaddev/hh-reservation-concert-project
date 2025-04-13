package kr.hhplus.be.server.reservations.infra;

import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.Reservation;
import kr.hhplus.be.server.reservations.domain.ReservationRepository;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultReservationRepository extends ReservationRepository,
    JpaRepository<UUID, Reservation> {

}
