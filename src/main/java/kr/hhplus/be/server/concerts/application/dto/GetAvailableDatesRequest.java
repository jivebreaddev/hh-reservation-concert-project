package kr.hhplus.be.server.concerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public class GetAvailableDatesRequest {

  private final LocalDateTime localDateTime;
  private final UUID concertId;

  public GetAvailableDatesRequest(LocalDateTime localDateTime, UUID concertId) {
    this.localDateTime = localDateTime;
    this.concertId = concertId;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
