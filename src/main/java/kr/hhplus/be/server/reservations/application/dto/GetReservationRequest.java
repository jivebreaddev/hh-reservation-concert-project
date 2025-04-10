package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class GetReservationRequest {
  private final UUID userId;

  public GetReservationRequest(UUID userId) {
    this.userId = userId;
  }

  public UUID getUserId() {
    return userId;
  }
}
