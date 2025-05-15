package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.dto.AvailableSeat;
import kr.hhplus.be.server.concerts.application.dto.ConcertRankingDto;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.reservations.application.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

public class RankingUseCaseIntegrationTest extends IntegrationTest {
  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  ConcertFixtureSaver saver;

  @Autowired
  RankingUseCase rankingUseCase;

  @Autowired
  private RedissonClient redissonClient;

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of("concerts"));
    clearAll();
  }

  public void clearAll() {
    redissonClient.getKeys().getKeysByPattern("ranking:*").forEach(redissonClient::getBucket);
    redissonClient.getKeys().deleteByPattern("ranking:*");
  }


  @Test
  @DisplayName("랭킹 정보를 추가할 경우, 랭킹 정보가 업데이트 됩니다.")
  void testTopConcertRankings() {
    // given
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    rankingUseCase.rankingUpdate("concert-A", 10);
    rankingUseCase.rankingUpdate("concert-B", 20);
    rankingUseCase.rankingUpdate("concert-C", 15);

    // when
    List<ConcertRankingDto> topN = rankingUseCase.getTopNConcerts(today);

    // then
    assertThat(topN).hasSize(3);
    assertThat(topN).extracting(ConcertRankingDto::getConcert)
        .containsExactly("concert-B", "concert-C", "concert-A");
  }

  @Test
  @DisplayName("동일한 콘서트 ID에 대해 여러 번 rankingUpdate가 호출되었을 때, 랭킹 정보가 오류 없이 업데이트 됩니다.")
  void testConsistentUpdate() throws InterruptedException {
    // given
    int numberOfThreads = 20;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);
    String concertId = "concert-1";
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    //when
    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          rankingUseCase.rankingUpdate(concertId, 10);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    // then
    RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet("ranking:" + today);
    assertThat(ranking.getScore(concertId)).isEqualTo(200.0);
  }

  @Test
  @DisplayName("인기 예약 정보가 존재하지 않을 때, 빈값으로 표기합니다.")
  void testWhenRedisIsEmptyReturnEmptyObjects() {
    // given
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    // when
    List<ConcertRankingDto> rankings = rankingUseCase.getTopNConcerts(today);

    // then
    assertThat(rankings).isEmpty();
  }

  @Test
  @DisplayName("매진된 특정일자의 콘서트가 있으면 중복 없이 boostScore가 적용된다.")
  void testDistinctBoostWhenSoldout() {
    // given
    UUID concertId = saver.soldOutConcertDate(LocalDateTime.now().plusDays(2));
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    // when
    rankingUseCase.applySoldOutScore();
    rankingUseCase.applySoldOutScore();

    // then
    RScoredSortedSet<String> rankingSet = redissonClient.getScoredSortedSet("ranking:" + today);
    Double score = rankingSet.getScore(concertId.toString());
    assertThat(score).isEqualTo(100_000.0);

    RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet("ranking:" + today);
    assertThat(ranking.getScore(concertId.toString())).isEqualTo(100_000.0);
  }
}
