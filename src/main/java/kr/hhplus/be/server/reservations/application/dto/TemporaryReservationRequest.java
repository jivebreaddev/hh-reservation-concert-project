package kr.hhplus.be.server.reservations.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class TemporaryReservationRequest {
  private final UUID userId;
  private final UUID seatId;
  private final UUID concertId;

  public TemporaryReservationRequest(UUID userId, UUID seatId, UUID concertId) {
    this.userId = userId;
    this.seatId = seatId;
    this.concertId = concertId;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getSeatId() {
    return seatId;
  }

  public UUID getConcertId() {
    return concertId;
  }
}
