package kr.hhplus.be.server.concerts.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConcertScheduleRepository {
  List<ConcertSchedules> findAllByConcertId(UUID uuid);

  List<ConcertSchedules> findByAvailableCountAndConcertDateGreaterThan(int availableCount, LocalDateTime now);

}
