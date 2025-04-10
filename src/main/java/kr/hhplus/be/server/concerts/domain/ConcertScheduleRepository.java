package kr.hhplus.be.server.concerts.domain;

import java.util.List;
import java.util.UUID;

public interface ConcertScheduleRepository {
  List<ConcertSchedules> findAllById(UUID uuid);

}
