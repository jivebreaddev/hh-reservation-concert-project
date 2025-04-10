package kr.hhplus.be.server.concerts.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class GetAvailableSeatsRequest {
  @NotBlank
  private final String concertId;

  public GetAvailableSeatsRequest(String concertId) {
    this.concertId = concertId;
  }

  public UUID getConcertId() {
    return UUID.fromString(concertId);
  }
}
