package kr.hhplus.be.server.reservations.domain.event;

import java.util.UUID;

public class SeatReservedStatusEvent {
  private final UUID seatId;
  private final UUID concertId;
  private SeatReservedStatusEvent(UUID seatId, UUID concertId) {
    this.seatId = seatId;
    this.concertId = concertId;
  }

  public static SeatReservedStatusEvent of(UUID seatId, UUID concertId) {
    return new SeatReservedStatusEvent(seatId, concertId);
  }

  public UUID getSeatId() {
    return seatId;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
