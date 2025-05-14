package kr.hhplus.be.server.concerts.domain;

import java.util.List;

public interface SeatRankingRepository {
  void increaseScore(String dateKey, String productId, int amount);
  List<ConcertRanking> getTopN(String dateKey, int limit);
}
