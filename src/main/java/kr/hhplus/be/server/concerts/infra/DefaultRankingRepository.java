package kr.hhplus.be.server.concerts.infra;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kr.hhplus.be.server.concerts.domain.ConcertRanking;
import kr.hhplus.be.server.concerts.domain.SeatRankingRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultRankingRepository implements SeatRankingRepository {

  private final RedissonClient redissonClient;

  public DefaultRankingRepository(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  @Override
  public void increaseScore(String concertKey, String dateKey, int amount) {
    RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet("ranking:" + dateKey);
    ranking.addScore(concertKey, amount);
    ranking.expire(Duration.ofDays(3));
  }

  @Override
  public List<ConcertRanking> getTopN(String dateKey, int limit) {
    RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet("ranking:" + dateKey);
    return ranking.entryRangeReversed(0, limit - 1)
        .stream()
        .map(rank -> ConcertRanking.of(rank.getValue(), dateKey, rank.getScore().longValue()))
        .collect(Collectors.toList());
  }

  @Override
  public void boostScore(String concertId, String dateKey, int amount) {
    String boostKey = "boosted:" + dateKey + ":" + concertId;
    RBucket<Boolean> boostedFlag = redissonClient.getBucket(boostKey);
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));


    if (boostedFlag.get() == null) {
      increaseScore(concertId, today, amount);
      boostedFlag.set(true, 30, TimeUnit.DAYS);
    }
  }
}
