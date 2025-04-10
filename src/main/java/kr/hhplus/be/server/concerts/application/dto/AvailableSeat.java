package kr.hhplus.be.server.concerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class AvailableSeat {
  private final UUID seatId;

  public AvailableSeat(UUID seatId) {

    this.seatId = seatId;
  }


  public UUID getSeatId() {
    return seatId;
  }
}
