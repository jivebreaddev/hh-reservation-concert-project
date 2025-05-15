package kr.hhplus.be.server.concerts.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

class SeatRankingRepositoryTest extends IntegrationTest {
  @SpyBean
  private SeatRankingRepository seatRankingRepository;

  @MockBean
  private RedissonClient redissonClient;

  @Test
  @DisplayName("increase Score 호출 시에 RedissonClient가 addScore와 scoredSortedSet을 호출합니다.")
  void increaseScore() {
    // given
    RScoredSortedSet<Object> mockSet = mock(RScoredSortedSet.class);
    given(redissonClient.getScoredSortedSet(anyString())).willReturn(mockSet);

    // when
    seatRankingRepository.increaseScore("concert-1", "20240514", 10);

    // then
    verify(redissonClient).getScoredSortedSet("ranking:20240514");
    verify(mockSet).addScore("concert-1", 10);
  }

  @Test
  @DisplayName("topN 호출 시에 RedissonClient가 sortedSet에서 높은 점수 순으로을 조회하는것을 검증")
  void getTopNInRedissonClient() {
    // given
    RScoredSortedSet<Object> mockSet = mock(RScoredSortedSet.class);
    List<ScoredEntry<Object>> mockedEntries = List.of(
        new ScoredEntry<>(100.0, "concert-1"),
        new ScoredEntry<>(90.0, "concert-2")
    );

    given(redissonClient.getScoredSortedSet("ranking:20240514")).willReturn(mockSet);
    given(mockSet.entryRangeReversed(0, 1)).willReturn(mockedEntries);

    // when
    List<ConcertRanking> result = seatRankingRepository.getTopN("20240514", 2);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getConcert()).isEqualTo("concert-1");
    assertThat(result.get(1).getConcert()).isEqualTo("concert-2");

  }

  @Test
  @DisplayName("콘서트 스케줄이 매진시, 점수를 크게 증가 시킵니다.")
  void boostScoreWhenSoldout() {
    // given
    RScoredSortedSet<Object> mockSet = mock(RScoredSortedSet.class);
    given(redissonClient.getScoredSortedSet(anyString())).willReturn(mockSet);

    String concertId = "concert-1";
    String dateKey = "20250515";
    String boostKey = "boosted:" + dateKey + ":" + concertId;

    RBucket<Object> bucket = mock(RBucket.class);
    when(redissonClient.getBucket(boostKey)).thenReturn(bucket);
    when(bucket.get()).thenReturn(null);

    // when
    seatRankingRepository.boostScore(concertId, dateKey, 10_000);

    // then
    verify(bucket).set(true, 30, TimeUnit.DAYS);
    verify(seatRankingRepository).increaseScore(concertId, dateKey, 10_000);
    verify(redissonClient).getScoredSortedSet("ranking:20250515");
    verify(mockSet).addScore("concert-1", 10_000);
  }
}
