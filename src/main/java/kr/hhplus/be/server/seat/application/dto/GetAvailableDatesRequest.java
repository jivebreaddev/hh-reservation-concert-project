package kr.hhplus.be.server.seat.application.dto;

import java.time.LocalDateTime;

public class GetAvailableDatesRequest {

  private final LocalDateTime localDateTime;
  private final Long concertId;

  public GetAvailableDatesRequest(LocalDateTime localDateTime, Long concertId) {
    this.localDateTime = localDateTime;
    this.concertId = concertId;
  }


}
