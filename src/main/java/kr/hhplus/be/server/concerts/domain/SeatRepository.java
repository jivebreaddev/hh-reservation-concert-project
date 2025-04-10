package kr.hhplus.be.server.concerts.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository {
  List<Seat> findAllByConcertIdAndSeatStatus(UUID uuid, SeatStatus seatStatus);

  Optional<Seat> findByIdAndSeatStatus(UUID seatId, SeatStatus seatStatus);

  Seat save(Seat seat);
}

