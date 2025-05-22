package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.dto.AvailableSeat;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.reservations.application.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.application.event.SeatReservedStatusEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;

public class SeatStatusChangeListenerIntegrationTest extends IntegrationTest {


  @Autowired
  ConcertFixtureSaver saver;
  @Autowired
  ApplicationEventPublisher eventPublisher;
  @Autowired
  DefaultSeatQueryService defaultSeatQueryService;
  @Autowired
  SeatQueryUseCase seatQueryUseCase;
  @Autowired
  TransactionTemplate transactionTemplate;
  @Autowired
  SeatStatusChangeListener seatStatusChangeListener;
  @Autowired
  private RedissonClient redissonClient;
  private static UUID CONCERT_ID;
  private static List<LocalDateTime> DATES = List.of(
      LocalDateTime.of(2030, 4, 17, 12, 0),
      LocalDateTime.of(2030, 4, 18, 12, 0),
      LocalDateTime.of(2030, 4, 19, 12, 0));

  @BeforeEach
  void cleanDatabase() {
    CONCERT_ID = saver.saveHalfHeldConcertData(DATES);
  }

  @Test
  @DisplayName("임시예약 이벤트 발행시, 50개가 예약된 콘서트에서 1개의 좌석이 임시예약 상태로 들어갑니다.")
  void testHandleAvailableStatusEventAfterCommit() {
    // Given
    GetAvailableSeatsResponse response = defaultSeatQueryService
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));
    UUID seat = response.getAvailableSeats().stream()
        .map(AvailableSeat::getSeatId)
        .findFirst().get();

    // When
    seatStatusChangeListener.handleHeld(SeatHeldStatusEvent.of(seat));

    List<AvailableSeat> seats = seatQueryUseCase
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID))
        .getAvailableSeats();
    assertThat(seats).hasSize(49);
  }

  @Test
  @DisplayName("예약 이벤트 1개 발행시, 랭킹 정보가 업데이트 됩니다.")
  void testReservedConcertIncreaseRankings() {
    // Given
    GetAvailableSeatsResponse response = defaultSeatQueryService
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));
    UUID seat = response.getAvailableSeats().stream()
        .map(AvailableSeat::getSeatId)
        .findFirst().get();
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    // When
    seatStatusChangeListener.handleHeld(SeatHeldStatusEvent.of(seat));
    seatStatusChangeListener.handleReserved(SeatReservedStatusEvent.of(seat, CONCERT_ID));

    // Then
    RScoredSortedSet<String> ranking = redissonClient.getScoredSortedSet("ranking:" + today);

    Double score = ranking.getScore(CONCERT_ID.toString());
    System.out.println("Score of concertKey (" + CONCERT_ID.toString() + "): " + score);
    RKeys keys = redissonClient.getKeys();
    Iterable<String> allKeys = keys.getKeys();

    for (String key : allKeys) {
      System.out.println("Key: " + key);
    }

    assertThat(ranking.getScore(CONCERT_ID.toString())).isEqualTo(1.0);
  }

}
