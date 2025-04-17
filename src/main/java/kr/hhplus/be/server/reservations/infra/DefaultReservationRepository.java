package kr.hhplus.be.server.reservations.infra;

import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.Reservation;
import kr.hhplus.be.server.reservations.domain.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DefaultReservationRepository extends ReservationRepository,
    JpaRepository<Reservation, UUID> {

}
