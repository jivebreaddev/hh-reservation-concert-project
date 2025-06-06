package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
@Schema
public class ReservationResponse {
  private UUID reservationId;
  private UUID userId;
  private UUID seatId;
  private ReservationStatus reservationStatus;

  public ReservationResponse() {
  }

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

  public void setReservationId(UUID reservationId) {
    this.reservationId = reservationId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setSeatId(UUID seatId) {
    this.seatId = seatId;
  }

  public void setReservationStatus(
      ReservationStatus reservationStatus) {
    this.reservationStatus = reservationStatus;
  }
}
