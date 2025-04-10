package kr.hhplus.be.server.concerts.application;

import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;

public interface SeatQueryUseCase {

  GetAvailableSeatsResponse getAvailableSeatsResponseList(GetAvailableSeatsRequest request);

  void changeToAvailable(UUID seatId);

  void changeToReserved(UUID seatId);

  void changeToHeld(UUID seatId);
}
