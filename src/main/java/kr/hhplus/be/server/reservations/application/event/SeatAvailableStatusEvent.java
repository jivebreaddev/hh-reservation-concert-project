package kr.hhplus.be.server.reservations.application.event;

import java.util.UUID;

public class SeatAvailableStatusEvent {
  private final UUID seatId;

  private SeatAvailableStatusEvent(UUID seatId) {
    this.seatId = seatId;
  }

  public static SeatAvailableStatusEvent of(UUID seatId) {
    return new SeatAvailableStatusEvent(seatId);
  }

  public UUID getSeatId() {
    return seatId;
  }
}
