package kr.hhplus.be.server.reservation.application.dto;

public class TemporaryReservationRequest {
  private final Long userId;
  private final Long seatId;

  public TemporaryReservationRequest(Long userId, Long seatId) {
    this.userId = userId;
    this.seatId = seatId;
  }
}
