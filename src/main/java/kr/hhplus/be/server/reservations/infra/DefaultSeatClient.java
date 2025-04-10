package kr.hhplus.be.server.reservations.infra;

import java.util.UUID;
import kr.hhplus.be.server.concerts.domain.SeatRepository;
import kr.hhplus.be.server.concerts.domain.SeatStatus;
import kr.hhplus.be.server.reservations.domain.SeatClient;
import org.springframework.stereotype.Component;

@Component
public class DefaultSeatClient implements SeatClient {

  private final SeatRepository seatRepository;

  public DefaultSeatClient(SeatRepository seatRepository) {
    this.seatRepository = seatRepository;
  }

  @Override
  public boolean seatAvailable(UUID seatId) {
    return seatRepository.findByIdAndSeatStatus(seatId, SeatStatus.AVAILABLE).isPresent();
  }

}
