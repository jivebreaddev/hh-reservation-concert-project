package kr.hhplus.be.server.concerts.domain;

import java.util.List;

public interface SeatRankingRepository {
  void increaseScore(String concertId, String dateKey, int amount);
  List<ConcertRanking> getTopN(String dateKey, int limit);

  void boostScore(String concertId, String dateKey, int amount);

}
