package kr.hhplus.be.server.reservation.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class ReservationRequest {
  private final Long userId;
  private final Long seatId;

  public ReservationRequest(Long userId, Long seatId) {
    this.userId = userId;
    this.seatId = seatId;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getSeatId() {
    return seatId;
  }
}
