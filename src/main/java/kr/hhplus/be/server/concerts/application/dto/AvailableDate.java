package kr.hhplus.be.server.concerts.application.dto;

import java.time.LocalDateTime;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;

public class AvailableDate {

  private LocalDateTime date;
  private Long remainingSeats;

  private AvailableDate(LocalDateTime date, Long remainingSeats) {
    this.date = date;
    this.remainingSeats = remainingSeats;
  }

  public static AvailableDate from(ConcertSchedules concertSchedules) {
    return new AvailableDate(concertSchedules.getConcertDate(),
        concertSchedules.getAvailableCount());
  }
}
