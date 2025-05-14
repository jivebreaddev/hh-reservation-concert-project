package kr.hhplus.be.server.concerts.application.dto;

import java.time.LocalDateTime;
import kr.hhplus.be.server.concerts.domain.ConcertRanking;

public class ConcertRankingDto {

  private final String concert;
  private final String date;
  private final Long counts;

  public ConcertRankingDto(String concert, String date, Long counts) {
    this.concert = concert;
    this.date = date;
    this.counts = counts;
  }

  public static ConcertRankingDto of(ConcertRanking ranking) {
    return new ConcertRankingDto(ranking.getConcert(), ranking.getDate(), ranking.getCounts());
  }
}
