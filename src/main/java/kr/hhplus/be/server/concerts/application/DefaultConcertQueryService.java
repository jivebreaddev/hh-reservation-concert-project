package kr.hhplus.be.server.concerts.application;

import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultConcertQueryService implements ConcertQueryUseCase {

  private final ConcertScheduleRepository concertScheduleRepository;

  protected DefaultConcertQueryService(ConcertScheduleRepository concertScheduleRepository) {
    this.concertScheduleRepository = concertScheduleRepository;
  }

  @Transactional(readOnly = true)
  public GetAvailableDatesResponse getAvailableDatesResponseList(UUID concertId) {
    return GetAvailableDatesResponse.of(concertScheduleRepository.findAllById(concertId));
  }
}
