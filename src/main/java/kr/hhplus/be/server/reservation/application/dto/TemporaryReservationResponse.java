package kr.hhplus.be.server.reservation.application.dto;

import kr.hhplus.be.server.reservation.domain.ReservationStatus;

public class TemporaryReservationResponse {
  private final Long userId;
  private final Long concertId;
  private final ReservationStatus reservationStatus;

  public TemporaryReservationResponse(Long userId, Long concertId,
      ReservationStatus reservationStatus) {
    this.userId = userId;
    this.concertId = concertId;
    this.reservationStatus = reservationStatus;
  }
}
