package kr.hhplus.be.server.fixture.concerts;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.concerts.domain.Concert;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;
import kr.hhplus.be.server.concerts.domain.Seat;
import kr.hhplus.be.server.concerts.domain.SeatStatus;

public class ConcertFixture {
  public static Concert createConcert() {
    return Concert.of(
        "서울 올림픽 경기장"
    );
  }

  public static ConcertSchedules createConcertSchedule(UUID concertId, LocalDateTime availableDate) {
    return ConcertSchedules.of(
        UUID.randomUUID(),
        concertId,
        100L,
        availableDate
    );
  }

  public static ConcertSchedules createSoldoutConcertSchedule(UUID concertId, LocalDateTime availableDate) {
    return ConcertSchedules.of(
        UUID.randomUUID(),
        concertId,
        0L,
        availableDate
    );
  }

  public static Seat createSeat(UUID concertId) {
    return Seat.of(
        UUID.randomUUID(),
        concertId,
        SeatStatus.AVAILABLE,
        LocalDateTime.now()
    );
  }

  public static Seat createHeldSeat(UUID concertId) {
    return Seat.of(
        UUID.randomUUID(),
        concertId,
        SeatStatus.HELD,
        LocalDateTime.now()
    );
  }

  public static Seat createReservedSeat(UUID concertId) {
    return Seat.of(
        UUID.randomUUID(),
        concertId,
        SeatStatus.RESERVED,
        LocalDateTime.now()
    );
  }
}
