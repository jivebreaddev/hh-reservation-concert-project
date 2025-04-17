package kr.hhplus.be.server.reservations.application.event;

import java.util.UUID;

public class SeatHeldStatusEvent {

  private final UUID seatId;

  private SeatHeldStatusEvent(UUID seatId) {
    this.seatId = seatId;
  }

  public static SeatHeldStatusEvent of(UUID seatId) {
    return new SeatHeldStatusEvent(seatId);
  }

  public UUID getSeatId() {
    return seatId;
  }
}
