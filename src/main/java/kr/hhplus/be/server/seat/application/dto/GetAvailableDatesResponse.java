package kr.hhplus.be.server.seat.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class GetAvailableDatesResponse {

  private final AvailableSeats availableSeats;


  public GetAvailableDatesResponse(AvailableSeats availableSeats) {
    this.availableSeats = availableSeats;
  }

  public AvailableSeats getAvailableSeats() {
    return availableSeats;
  }
}
