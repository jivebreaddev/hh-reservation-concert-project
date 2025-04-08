package kr.hhplus.be.server.concerts.domain;

import java.util.List;
import java.util.UUID;

public interface SeatRepository {
  List<Seat> findAllByIdInAndSeatStatus(UUID uuid, SeatStatus seatStatus);

}
