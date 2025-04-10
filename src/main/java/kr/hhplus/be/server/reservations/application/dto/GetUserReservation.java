package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
@Schema
public class GetUserReservation {

  private final LocalDateTime reservedDate;
  private final UUID seatId;
  private final ReservationStatus reservationStatus;

  public GetUserReservation(LocalDateTime reservedDate, UUID seatId, ReservationStatus reservationStatus) {
    this.reservedDate = reservedDate;
    this.seatId = seatId;
    this.reservationStatus = reservationStatus;
  }

  public LocalDateTime getReservedDate() {
    return reservedDate;
  }

  public UUID getSeatId() {
    return seatId;
  }

  public ReservationStatus getReservationStatus() {
    return reservationStatus;
  }
}
