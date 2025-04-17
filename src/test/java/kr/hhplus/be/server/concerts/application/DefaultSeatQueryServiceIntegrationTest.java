package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.dto.AvailableSeat;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultSeatQueryServiceIntegrationTest extends IntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  ConcertFixtureSaver saver;
  @Autowired
  DefaultSeatQueryService defaultSeatQueryService;

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

  @Nested
  @DisplayName("특정일의 콘서트 좌석을 조회할 때, ")
  class Concert_Seats_Query {

    @Test
    @DisplayName("예약가능한 좌석들이 조회된다.")
    void getConcertAvailableDates() {
      // When
      GetAvailableSeatsResponse response = defaultSeatQueryService
          .getAvailableSeatsResponseList(new GetAvailableSeatsRequest(CONCERT_ID));

      // Then
      List<UUID> seats = response.getAvailableSeats()
          .stream()
          .map(AvailableSeat::getSeatId)
          .toList();

      assertAll(
          () -> assertThat(seats).isNotEmpty(),
          () -> assertThat(response.getAvailableSeats()).isInstanceOf(List.class),
          () -> assertThat(response.getAvailableSeats()).hasSize(100)
      );

    }

  }
}
