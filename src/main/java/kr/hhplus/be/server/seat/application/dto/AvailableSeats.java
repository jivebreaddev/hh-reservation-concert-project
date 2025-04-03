package kr.hhplus.be.server.seat.application.dto;

import java.util.List;

public class AvailableSeats {

  private final List<AvailableSeat> availableSeatList;

  public AvailableSeats(List<AvailableSeat> availableSeatList) {
    this.availableSeatList = availableSeatList;

  }
}
