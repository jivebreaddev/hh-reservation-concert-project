package kr.hhplus.be.server.reservation.application.dto;

public class ReservationRequest {
  private final Long userId;
  private final Long seatId;

  public ReservationRequest(Long userId, Long seatId) {
    this.userId = userId;
    this.seatId = seatId;
  }
}
