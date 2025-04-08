package kr.hhplus.be.server.concerts.application.dto;

import java.util.UUID;

public class GetAvailableSeatsRequest {
  private final UUID concertId;

  public GetAvailableSeatsRequest(UUID concertId) {
    this.concertId = concertId;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
