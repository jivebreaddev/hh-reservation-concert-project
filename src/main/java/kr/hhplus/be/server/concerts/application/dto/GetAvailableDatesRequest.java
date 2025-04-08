package kr.hhplus.be.server.concerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
@Schema
public class GetAvailableDatesRequest {

  private final LocalDateTime localDateTime;
  private final Long concertId;

  public GetAvailableDatesRequest(LocalDateTime localDateTime, Long concertId) {
    this.localDateTime = localDateTime;
    this.concertId = concertId;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public Long getConcertId() {
    return concertId;
  }
}
