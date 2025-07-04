package kr.hhplus.be.server.concerts.application.dto;

import java.util.List;
import kr.hhplus.be.server.concerts.domain.Seat;

public class GetAvailableSeatsResponse {

  private final List<AvailableSeat> availableSeats;

  private GetAvailableSeatsResponse(List<AvailableSeat> availableSeats) {
    this.availableSeats = availableSeats;
  }

  public static GetAvailableSeatsResponse of(List<Seat> seats) {
    return new GetAvailableSeatsResponse(
        seats.stream()
            .map(seat -> new AvailableSeat(seat.getId()))
            .toList());
  }

  public List<AvailableSeat> getAvailableSeats() {
    return availableSeats;
  }
}
