package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.dto.AvailableSeat;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.reservations.application.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.application.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

public class SeatStatusChangeListenerIntegrationTest extends IntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
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
}
