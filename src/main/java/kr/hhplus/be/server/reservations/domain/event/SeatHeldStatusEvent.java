package kr.hhplus.be.server.reservations.domain.event;

import java.util.UUID;

public class SeatHeldStatusEvent {

  private final UUID seatId;
  private final UUID concertId;

  private SeatHeldStatusEvent(UUID seatId, UUID concertId) {
    this.seatId = seatId;
    this.concertId = concertId;
  }

  public static SeatHeldStatusEvent of(UUID concertId, UUID seatId) {
    return new SeatHeldStatusEvent(concertId, seatId);
  }

  public UUID getSeatId() {
    return seatId;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
