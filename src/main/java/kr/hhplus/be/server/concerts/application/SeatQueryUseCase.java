package kr.hhplus.be.server.concerts.application;

import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;

public interface SeatQueryUseCase {

  GetAvailableSeatsResponse getAvailableSeatsResponseList(GetAvailableSeatsRequest request);
}
