package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class ReservationRequest {
  private UUID reservationId;

  public ReservationRequest(UUID reservationId) {
    this.reservationId = reservationId;
  }

  public ReservationRequest() {
  }

  public UUID getReservationId() {
    return reservationId;
  }

  public void setReservationId(UUID reservationId) {
    this.reservationId = reservationId;
  }
}
