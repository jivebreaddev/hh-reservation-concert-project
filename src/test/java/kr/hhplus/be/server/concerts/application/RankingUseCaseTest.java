package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.ConcertRankingDto;
import kr.hhplus.be.server.concerts.domain.ConcertRanking;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;
import kr.hhplus.be.server.concerts.domain.SeatRankingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RankingUseCaseTest {
  @Mock
  private SeatRankingRepository seatRankingRepository;

  @Mock
  private ConcertScheduleRepository concertScheduleRepository;

  @InjectMocks
  private RankingUseCase rankingUseCase;

  @Test
  @DisplayName("오늘 날짜 기준으로 ranking을 집계한다.")
  void testIncreaseRankingScoreToday() {
    // Given
    String concertId = "concert-123";
    int quantity = 7;
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    // When
    rankingUseCase.rankingUpdate(concertId, quantity);

    // Then
    verify(seatRankingRepository).increaseScore(eq(concertId), eq(today), eq(quantity));
  }

  @Test
  @DisplayName("top N개의 랭킹을 반환합니다.")
  void testGetConcertRankings() {
    // given
    String date = "20240514";
    List<String> mockRanking = List.of("A", "B", "C");
    List<ConcertRanking> concertRankings = List.of(ConcertRanking.of("A", date, 3L),
        ConcertRanking.of("B", date, 3L),
        ConcertRanking.of("C", date, 3L));
    when(seatRankingRepository.getTopN(date, 10)).thenReturn(concertRankings);

    // when
    List<ConcertRankingDto> result = rankingUseCase.getTopNConcerts(date);

    // then
    assertThat(result)
        .hasSize(3)
        .extracting(ConcertRankingDto::getConcert)
        .containsExactly("A", "B", "C");
  }

  @Test
  @DisplayName("redis가_비어있을때도_getTopNConcerts_정상작동한다")
  void testEmptyTopRankings() {
    // given
    String dateKey = "20240514";
    when(seatRankingRepository.getTopN(dateKey, 10)).thenReturn(Collections.emptyList());

    // when
    List<ConcertRankingDto> result = rankingUseCase.getTopNConcerts(dateKey);

    // then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("매진 콘서트 존재시에 boostScore 호출")
  void testApplyingBoostScoreWhenSoldout() {
    // given
    ConcertSchedules concert = ConcertSchedules.of(
        UUID.randomUUID(), // concertId
        UUID.randomUUID(),
        0L,
        LocalDateTime.now().plusDays(2)
    );

    given(concertScheduleRepository.findByAvailableCountAndConcertDateGreaterThan(
        eq(0), any(LocalDateTime.class))
    ).willReturn(List.of(concert));

    // when
    rankingUseCase.applySoldOutScore();

    // then
    String expectedDateKey = concert.getConcertDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    verify(seatRankingRepository).boostScore(
        eq(concert.getConcertId().toString()),
        eq(expectedDateKey),
        eq(100_000)
    );
  }
}
