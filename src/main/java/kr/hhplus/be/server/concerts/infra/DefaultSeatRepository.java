package kr.hhplus.be.server.concerts.infra;

import java.util.UUID;
import kr.hhplus.be.server.concerts.domain.Seat;
import kr.hhplus.be.server.concerts.domain.SeatRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultSeatRepository extends SeatRepository, JpaRepository<Seat, UUID> {

}

