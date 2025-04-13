package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
@Schema
public class ReservationResponse {
  private final UUID reservationId;
  private final UUID userId;
  private final UUID seatId;
  private final ReservationStatus reservationStatus;

  public ReservationResponse(UUID reservationId, UUID userId, UUID seatId,
      ReservationStatus reservationStatus) {
    this.reservationId = reservationId;
    this.userId = userId;
    this.seatId = seatId;
    this.reservationStatus = reservationStatus;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getSeatId() {
    return seatId;
  }

  public ReservationStatus getReservationStatus() {
    return reservationStatus;
  }
}
