package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import kr.hhplus.be.server.concerts.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultConcertQueryServiceTest {

  @Mock
  private ConcertScheduleRepository concertScheduleRepository;

  @InjectMocks
  private DefaultConcertQueryService concertScheduleService;

  @Test
  @DisplayName("공연 ID로 예약 가능한 날짜 목록을 조회한다")
  void getAvailableDates() {
    // given
    UUID concertId = UUID.randomUUID();
    List<ConcertSchedules> schedules = List.of(mock(ConcertSchedules.class));

    when(concertScheduleRepository.findAllByConcertId(concertId)).thenReturn(schedules);

    // when
    GetAvailableDatesResponse response = concertScheduleService.getAvailableDatesResponseList(concertId);

    // then
    assertThat(response).isNotNull();
    verify(concertScheduleRepository).findAllByConcertId(concertId);
  }
}
