package kr.hhplus.be.server.reservations.domain.event;

import java.util.UUID;

public class SeatAvailableStatusEvent {
  private final UUID seatId;
  private final UUID concertId;

  private SeatAvailableStatusEvent(UUID seatId, UUID concertId) {
    this.seatId = seatId;
    this.concertId = concertId;
  }

  public static SeatAvailableStatusEvent of(UUID concertId, UUID seatId) {
    return new SeatAvailableStatusEvent(seatId, concertId);
  }

  public UUID getSeatId() {
    return seatId;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
