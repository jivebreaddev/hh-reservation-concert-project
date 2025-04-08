package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
@Schema
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

  public LocalDateTime getReservedDate() {
    return reservedDate;
  }

  public Long getSeatId() {
    return seatId;
  }

  public String getSectionId() {
    return sectionId;
  }

  public Long getLineId() {
    return LineId;
  }

  public Long getConcertId() {
    return concertId;
  }

  public String getConcertName() {
    return concertName;
  }

  public ReservationStatus getReservationStatus() {
    return reservationStatus;
  }
}
