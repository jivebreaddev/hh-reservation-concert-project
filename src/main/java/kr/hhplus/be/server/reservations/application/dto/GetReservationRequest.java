package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class GetReservationRequest {
  private final Long userId;

  public GetReservationRequest(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
