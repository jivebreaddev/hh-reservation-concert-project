package kr.hhplus.be.server.reservations.application.event;

import java.util.UUID;

public class SeatPendingStatusEvent {
  private final UUID seatId;

  private SeatPendingStatusEvent(UUID seatId) {
    this.seatId = seatId;
  }

  public static SeatPendingStatusEvent of(UUID seatId) {
    return new SeatPendingStatusEvent(seatId);
  }

  public UUID getSeatId() {
    return seatId;
  }
}
