package kr.hhplus.be.server.concerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public class GetAvailableDatesRequest {

  private final LocalDateTime localDateTime;
  @NotBlank
  private final String concertId;

  public GetAvailableDatesRequest(LocalDateTime localDateTime, String concertId) {
    this.localDateTime = localDateTime;
    this.concertId = concertId;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public String getConcertId() {
    return concertId;
  }
}
