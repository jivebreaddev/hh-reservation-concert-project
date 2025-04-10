package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class ReservationRequest {
  private final UUID reservationId;

  public ReservationRequest(UUID reservationId) {
    this.reservationId = reservationId;
  }

  public UUID getReservationId() {
    return reservationId;
  }
}
