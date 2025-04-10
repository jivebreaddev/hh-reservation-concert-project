package kr.hhplus.be.server.concerts.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class GetAvailableSeatsRequest {
  @NotBlank
  private final UUID concertId;

  public GetAvailableSeatsRequest(UUID concertId) {
    this.concertId = concertId;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
