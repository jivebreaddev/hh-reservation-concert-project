package kr.hhplus.be.server.reservations.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.DefaultSeatQueryService;
import kr.hhplus.be.server.concerts.application.dto.AvailableSeat;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.reservations.application.dto.GetReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.GetReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.ReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.ReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationResponse;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultReservationServiceIntegrationTest extends IntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  ConcertFixtureSaver saver;
  @Autowired
  DefaultSeatQueryService defaultSeatQueryService;
  @Autowired
  DefaultReservationService defaultReservationService;
  private static UUID CONCERT_ID;
  private static List<LocalDateTime> DATES = List.of(
      LocalDateTime.of(2030, 4, 17, 12, 0),
      LocalDateTime.of(2030, 4, 18, 12, 0),
      LocalDateTime.of(2030, 4, 19, 12, 0));

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of("concerts", "concert_schedules", "seats"));
    CONCERT_ID = saver.saveConcertData(DATES);
  }

  @Test
  @DisplayName("임시예약할 때, 예약 상태가 PENDING 으로 저장된다.")
  void pendingReservation() {
    // Given
    GetAvailableSeatsResponse response = defaultSeatQueryService
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));
    UUID seat = response.getAvailableSeats().stream()
        .map(AvailableSeat::getSeatId)
        .findFirst().get();
    UUID userId = UUID.randomUUID();

    // When
    TemporaryReservationResponse reservationResponse = defaultReservationService.bookTemporarySeat(
        new TemporaryReservationRequest(userId, seat));

    //Then
    assertAll(
        () -> assertThat(reservationResponse.getReservationStatus()).isEqualTo(
            ReservationStatus.PENDING),
        () -> assertThat(reservationResponse.getSeatId()).isEqualTo(
            seat),
        () -> assertThat(reservationResponse.getUserId()).isEqualTo(
            userId)
    );
  }

  @Test
  @DisplayName("예약완료할 때, 예약 상태가 CONFIRMED 으로 저장된다.")
  void reserve() {
    // Given
    GetAvailableSeatsResponse response = defaultSeatQueryService
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));
    UUID seat = response.getAvailableSeats().stream()
        .map(AvailableSeat::getSeatId)
        .findFirst().get();
    UUID userId = UUID.randomUUID();

    // When
    TemporaryReservationResponse temporaryReservationResponse = defaultReservationService.bookTemporarySeat(
        new TemporaryReservationRequest(userId, seat));
    ReservationResponse reservationResponse = defaultReservationService.bookSeat(
        new ReservationRequest(temporaryReservationResponse.getReservationId()));

    //Then
    assertAll(
        () -> assertThat(reservationResponse.getReservationStatus()).isEqualTo(
            ReservationStatus.CONFIRMED),
        () -> assertThat(reservationResponse.getSeatId()).isEqualTo(
            seat),
        () -> assertThat(reservationResponse.getUserId()).isEqualTo(
            userId)
    );
  }

  @Test
  @DisplayName("유저 ID로 예약 내역을 조회할 수 있다.")
  void getReservations() {
    // Given
    GetAvailableSeatsResponse response = defaultSeatQueryService
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));
    UUID seat = response.getAvailableSeats().stream()
        .map(AvailableSeat::getSeatId)
        .findFirst().get();
    UUID userId = UUID.randomUUID();
    defaultReservationService.bookTemporarySeat(new TemporaryReservationRequest(userId, seat));

    // When
    GetReservationResponse reservationResponse = defaultReservationService.getReservations(
        new GetReservationRequest(userId));

    //Then
    assertAll(
        () -> assertThat(reservationResponse.getReservations()).hasSize(1),
        () -> assertThat(reservationResponse.getReservations().get(0).getSeatId()).isEqualTo(seat)
    );

  }

  @Test
  @DisplayName("예약 내역 없을때, 빈 리스트가 조회된다.")
  void getEmptyReservations() {
    // Given

    UUID userId = UUID.randomUUID();

    // When
    GetReservationResponse reservationResponse = defaultReservationService.getReservations(
        new GetReservationRequest(userId));

    //Then
    assertThat(reservationResponse.getReservations()).isEmpty();


  }

  @Test
  @DisplayName("동일 유저가 동시 예약 요청 시 하나만 성공해야 함")
  void concurrencyReserveTest() throws InterruptedException {
    // Given
    int numberOfThreads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    GetAvailableSeatsResponse response = defaultSeatQueryService
        .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));
    UUID userId = UUID.randomUUID();
    UUID seat = response.getAvailableSeats().stream()
        .map(AvailableSeat::getSeatId)
        .findFirst().get();
    List<Boolean> results = Collections.synchronizedList(new ArrayList<>());

    // When

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          defaultReservationService.bookTemporarySeat(new TemporaryReservationRequest(userId, seat));
          results.add(true);
        } catch (Exception e){
          results.add(false);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();
    // When
    long successCount = results.stream().filter(result -> result).count();
    assertThat(successCount).isEqualTo(1);

  }
}
