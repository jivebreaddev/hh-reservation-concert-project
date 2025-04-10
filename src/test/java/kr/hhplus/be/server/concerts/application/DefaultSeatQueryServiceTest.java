package kr.hhplus.be.server.concerts.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import kr.hhplus.be.server.concerts.domain.Seat;
import kr.hhplus.be.server.concerts.domain.SeatRepository;
import kr.hhplus.be.server.concerts.domain.SeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultSeatQueryServiceTest {

  @Mock
  private SeatRepository seatRepository;

  @InjectMocks
  private DefaultSeatQueryService seatService;

  @Test
  @DisplayName("공연 ID로 사용 가능한 좌석 목록을 조회한다")
  void getAvailableSeats() {
    UUID concertId = UUID.randomUUID();
    List<Seat> seats = List.of(mock(Seat.class));

    when(seatRepository.findAllByConcertIdAndSeatStatus(concertId, SeatStatus.AVAILABLE))
        .thenReturn(seats);

    GetAvailableSeatsRequest request = new GetAvailableSeatsRequest(concertId);

    GetAvailableSeatsResponse response = seatService.getAvailableSeatsResponseList(request);

    assertThat(response).isNotNull();
    verify(seatRepository).findAllByConcertIdAndSeatStatus(concertId, SeatStatus.AVAILABLE);
  }

}
