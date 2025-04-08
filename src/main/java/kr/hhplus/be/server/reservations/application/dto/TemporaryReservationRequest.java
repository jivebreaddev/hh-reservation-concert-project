package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class TemporaryReservationRequest {
  private final Long userId;
  private final Long seatId;

  public TemporaryReservationRequest(Long userId, Long seatId) {
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
