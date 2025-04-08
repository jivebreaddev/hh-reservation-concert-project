package kr.hhplus.be.server.concert.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
@Schema
public class AvailableSeats {

  private final List<AvailableSeat> availableSeatList;

  public AvailableSeats(List<AvailableSeat> availableSeatList) {
    this.availableSeatList = availableSeatList;

  }

  public List<AvailableSeat> getAvailableSeatList() {
    return availableSeatList;
  }
}
