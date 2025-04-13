package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class TemporaryReservationRequest {
  private final UUID userId;
  private final UUID seatId;

  public TemporaryReservationRequest(UUID userId, UUID seatId) {
    this.userId = userId;
    this.seatId = seatId;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getSeatId() {
    return seatId;
  }
}
