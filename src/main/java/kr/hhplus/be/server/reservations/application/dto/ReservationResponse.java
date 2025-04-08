package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
@Schema
public class ReservationResponse {

  private final Long userId;
  private final Long concertId;
  private final ReservationStatus reservationStatus;

  public ReservationResponse(Long userId, Long concertId,
      ReservationStatus reservationStatus) {
    this.userId = userId;
    this.concertId = concertId;
    this.reservationStatus = reservationStatus;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getConcertId() {
    return concertId;
  }

  public ReservationStatus getReservationStatus() {
    return reservationStatus;
  }
}
