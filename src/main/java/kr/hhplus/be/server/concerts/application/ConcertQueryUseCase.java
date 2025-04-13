package kr.hhplus.be.server.concerts.application;

import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ConcertQueryUseCase {

  GetAvailableDatesResponse getAvailableDatesResponseList(UUID concertId);
}
