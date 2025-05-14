package kr.hhplus.be.server.concerts.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.hhplus.be.server.concerts.application.dto.ConcertRankingDto;
import kr.hhplus.be.server.concerts.domain.SeatRankingRepository;
import org.springframework.stereotype.Component;

@Component
public class RankingUseCase {

  private final SeatRankingRepository seatRankingRepository;

  protected RankingUseCase(SeatRankingRepository seatRankingRepository) {
    this.seatRankingRepository = seatRankingRepository;
  }

  public void rankingUpdate(String concertID, int quantity) {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    seatRankingRepository.increaseScore(today, concertID, quantity);
  }

  public List<ConcertRankingDto> getTopNConcerts(String date) {
    return seatRankingRepository.getTopN(date, 10)
        .stream()
        .map(ranking -> ConcertRankingDto.of(ranking))
        .toList();
  }
}
