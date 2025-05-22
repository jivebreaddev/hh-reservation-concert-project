package kr.hhplus.be.server.concerts.domain;

public class ConcertRanking {
  private final String concert;

  private final String date;
  private final Long counts;

  private ConcertRanking(String concert, String date, Long counts) {
    this.concert = concert;
    this.date = date;
    this.counts = counts;
  }

  public static ConcertRanking of(String concert, String date, Long counts) {
    return new ConcertRanking(concert, date, counts);
  }

  public String getConcert() {
    return concert;
  }
  public String getDate() {
    return date;
  }
  public Long getCounts() {
    return counts;
  }
}
