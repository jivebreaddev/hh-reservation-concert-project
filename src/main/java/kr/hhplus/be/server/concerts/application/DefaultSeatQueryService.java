package kr.hhplus.be.server.concerts.application;

import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.concerts.domain.Seat;
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
        seatRepository.findAllByConcertIdAndSeatStatus(request.getConcertId(), SeatStatus.AVAILABLE));
  }

  @Override
  public void changeToAvailable(UUID seatId) {
    Seat seat = seatRepository.findByIdAndSeatStatus(seatId, SeatStatus.HELD)
        .orElseThrow(RuntimeException::new);;

    seat.toAvailable();
    seatRepository.save(seat);
  }

  @Override
  public void changeToHeld(UUID seatId) {
    Seat seat = seatRepository.findByIdAndSeatStatus(seatId, SeatStatus.AVAILABLE)
        .orElseThrow(RuntimeException::new);

    seat.toHeld();
    seatRepository.save(seat);
  }

  @Override
  public void changeToReserved(UUID seatId) {
    Seat seat = seatRepository.findByIdAndSeatStatus(seatId, SeatStatus.HELD)
        .orElseThrow(RuntimeException::new);

    seat.toReserved();
    seatRepository.save(seat);
  }
}
