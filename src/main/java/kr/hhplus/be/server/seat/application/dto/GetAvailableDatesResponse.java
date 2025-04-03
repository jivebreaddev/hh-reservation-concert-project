package kr.hhplus.be.server.seat.application.dto;

public class GetAvailableDatesResponse {

  private final AvailableSeats availableSeats;


  public GetAvailableDatesResponse(AvailableSeats availableSeats) {
    this.availableSeats = availableSeats;
  }
}
