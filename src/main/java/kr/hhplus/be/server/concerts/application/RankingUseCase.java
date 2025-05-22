package kr.hhplus.be.server.concerts.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.hhplus.be.server.concerts.application.dto.ConcertRankingDto;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;
import kr.hhplus.be.server.concerts.domain.SeatRankingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RankingUseCase {
  private static int ZERO = 0;
  private static int BOOST_SCORE = 100000;
  private final SeatRankingRepository seatRankingRepository;
  private final ConcertScheduleRepository concertScheduleRepository;

  protected RankingUseCase(SeatRankingRepository seatRankingRepository,
      ConcertScheduleRepository concertScheduleRepository) {
    this.seatRankingRepository = seatRankingRepository;
    this.concertScheduleRepository = concertScheduleRepository;
  }

  public void rankingUpdate(String concertID, int quantity) {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    seatRankingRepository.increaseScore(concertID, today, quantity);
  }

  public List<ConcertRankingDto> getTopNConcerts(String date) {

    return seatRankingRepository.getTopN(date, 10)
        .stream()
        .map(ranking -> ConcertRankingDto.of(ranking))
        .toList();
  }

  @Scheduled(cron = "0 0/5 * * * *")
  public void applySoldOutScore() {
    List<ConcertSchedules> soldOutConcertSchedules = concertScheduleRepository.findByAvailableCountAndConcertDateGreaterThan(ZERO,
        LocalDateTime.now());

    for (ConcertSchedules concert : soldOutConcertSchedules) {
      String concertDate = concert.getConcertDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

      seatRankingRepository.boostScore(concert.getConcertId().toString(),
          concertDate, BOOST_SCORE);
    }

  }
}
