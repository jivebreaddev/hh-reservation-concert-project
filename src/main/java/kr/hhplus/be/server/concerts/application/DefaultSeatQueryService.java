package kr.hhplus.be.server.concerts.application;

import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.concerts.domain.SeatRepository;
import kr.hhplus.be.server.concerts.domain.SeatStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultSeatQueryService implements SeatQueryUseCase {

  private final SeatRepository seatRepository;

  protected DefaultSeatQueryService(SeatRepository seatRepository) {
    this.seatRepository = seatRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public GetAvailableSeatsResponse getAvailableSeatsResponseList(GetAvailableSeatsRequest request) {
    return GetAvailableSeatsResponse.of(
        seatRepository.findAllByIdInAndSeatStatus(request.getConcertId(), SeatStatus.AVAILABLE));
  }

}
