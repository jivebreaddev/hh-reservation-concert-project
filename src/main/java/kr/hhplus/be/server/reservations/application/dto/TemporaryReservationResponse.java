package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
@Schema
public class TemporaryReservationResponse {
  private final UUID userId;
  private final UUID seatId;
  private final UUID reservationId;
  private final ReservationStatus reservationStatus;

  public TemporaryReservationResponse(UUID userId, UUID seatId, UUID reservationId,
      ReservationStatus reservationStatus) {
    this.userId = userId;
    this.seatId = seatId;
    this.reservationId = reservationId;
    this.reservationStatus = reservationStatus;
  }

  public UUID getReservationId() {
    return reservationId;
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
