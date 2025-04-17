package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.dto.AvailableDate;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import kr.hhplus.be.server.fixture.concerts.ConcertFixtureSaver;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class DefaultConcertQueryServiceIntegrationTest extends IntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  ConcertFixtureSaver saver;
  @Autowired
  DefaultConcertQueryService defaultConcertQueryService;

  private static UUID CONCERT_ID;
  private static List<LocalDateTime> DATES = List.of(
      LocalDateTime.of(2030, 4, 17, 12, 0),
      LocalDateTime.of(2030, 4, 18, 12, 0),
      LocalDateTime.of(2030, 4, 19, 12, 0));

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of());
    CONCERT_ID = saver.saveConcertData(DATES);

  }

  @Nested
  @DisplayName("콘서트 일정을 조회할 때, ")
  class Concert_Schedule_Query {

    @Test
    @DisplayName("콘서트 예약 가능한 날짜를 응답 받는다.")
    void getConcertAvailableDates() {
      // When
      GetAvailableDatesResponse response = defaultConcertQueryService.
          getAvailableDatesResponseList(CONCERT_ID);

      // Then
      List<LocalDateTime> dates = response.getAvailableDates()
          .stream()
          .map(date -> date.getDate())
          .toList();
      assertAll(
          () -> assertThat(response.getAvailableDates()).isNotEmpty(),
          () -> assertThat(response.getAvailableDates()).isInstanceOf(List.class),
          () -> assertThat(dates).hasSameSizeAs(DATES),
          () -> assertThat(dates).containsExactlyInAnyOrderElementsOf(DATES)
      );

    }

    @Test
    @DisplayName("존재하지 않는 콘서트 예약 가능한 날짜는 빈값을 받는다.")
    void getNotExistingConcertAvailableDates() {
      // When
      GetAvailableDatesResponse response = defaultConcertQueryService.
          getAvailableDatesResponseList(UUID.randomUUID());
      List<AvailableDate> dates = response.getAvailableDates();


      // Then
      assertAll(
          () -> assertThat(response.getAvailableDates()).isEmpty(),
          () -> assertThat(response.getAvailableDates()).isInstanceOf(List.class),
          () -> assertThat(response.getAvailableDates().get(0)).isInstanceOf(AvailableDate.class),
          () -> assertThat(response.getAvailableDates().get(0).getRemainingSeats()).isEqualTo(100)
      );

    }

  }
}
