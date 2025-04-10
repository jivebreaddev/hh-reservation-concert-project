package kr.hhplus.be.server.concerts.infra;

import java.util.UUID;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultConcertRepository extends ConcertScheduleRepository,
    JpaRepository<UUID, ConcertSchedules> {

}
