package kr.hhplus.be.server.reservation.application.dto;

import java.time.LocalDateTime;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;

public class GetReservationResponse {

  private final LocalDateTime reservedDate;
  private final Long seatId;
  private final String sectionId;
  private final Long LineId;

  private final Long concertId;
  private final String concertName;
  private final ReservationStatus reservationStatus;

  public GetReservationResponse(LocalDateTime reservedDate, Long seatId, String sectionId,
      Long lineId, Long concertId, String concertName, ReservationStatus reservationStatus) {
    this.reservedDate = reservedDate;
    this.seatId = seatId;
    this.sectionId = sectionId;
    LineId = lineId;
    this.concertId = concertId;
    this.concertName = concertName;
    this.reservationStatus = reservationStatus;
  }
}
