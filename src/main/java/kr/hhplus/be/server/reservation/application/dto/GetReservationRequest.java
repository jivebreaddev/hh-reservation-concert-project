package kr.hhplus.be.server.reservation.application.dto;

public class GetReservationRequest {
  private final Long userId;

  public GetReservationRequest(Long userId) {
    this.userId = userId;
  }
}
