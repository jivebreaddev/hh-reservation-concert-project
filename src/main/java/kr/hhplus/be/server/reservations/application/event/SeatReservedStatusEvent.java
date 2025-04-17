package kr.hhplus.be.server.reservations.application.event;

import java.util.UUID;

public class SeatReservedStatusEvent {
  private final UUID seatId;

  private SeatReservedStatusEvent(UUID seatId) {
    this.seatId = seatId;
  }

  public static SeatReservedStatusEvent of(UUID seatId) {
    return new SeatReservedStatusEvent(seatId);
  }

  public UUID getSeatId() {
    return seatId;
  }
}
