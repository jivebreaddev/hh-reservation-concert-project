package kr.hhplus.be.server.reservations.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.reservations.application.dto.GetReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.GetReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.ReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.ReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationResponse;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.Reservation;
import kr.hhplus.be.server.reservations.domain.ReservationRepository;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
import kr.hhplus.be.server.reservations.domain.SeatClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class DefaultReservationServiceTest {
  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private SeatClient seatClient;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private DefaultReservationService reservationService;

  private static final UUID concertId = UUID.randomUUID();
  @Test
  @DisplayName("유저 ID로 예약 목록을 조회하면 예약 응답이 반환된다")
  void getReservations() {
    UUID userId = UUID.randomUUID();
    List<Reservation> reservations = List.of(mock(Reservation.class));
    when(reservationRepository.findAllByUserId(userId)).thenReturn(reservations);

    GetReservationRequest request = new GetReservationRequest(userId);
    GetReservationResponse response = reservationService.getReservations(request);

    assertThat(response).isNotNull();
  }

  @Test
  @DisplayName("좌석이 사용 가능하면 임시 예약을 생성하고 이벤트를 발행한다")
  void bookTemporarySeat() {
    UUID seatId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    TemporaryReservationRequest request = new TemporaryReservationRequest(userId, seatId, concertId);

    when(seatClient.seatAvailable(seatId)).thenReturn(true);

    Reservation saved = Reservation.createTemporaryReservation(UUID.randomUUID(), userId, seatId, concertId, 5L);
    when(reservationRepository.save(any())).thenReturn(saved);

    TemporaryReservationResponse response = reservationService.bookTemporarySeat(request);

    assertThat(response.getSeatId()).isEqualTo(seatId);
    verify(eventPublisher).publishEvent(any(SeatPendingStatusEvent.class));
  }

  @Test
  @DisplayName("좌석이 사용 불가능하면 임시 예약 시 RuntimeException이 발생한다")
  void bookTemporarySeatNotAvailable() {
    UUID seatId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    TemporaryReservationRequest request = new TemporaryReservationRequest(userId, seatId, concertId);

    when(seatClient.seatAvailable(seatId)).thenReturn(false);

    assertThatThrownBy(() -> reservationService.bookTemporarySeat(request))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("예약 ID와 상태로 예약을 조회하고 좌석 예약을 확정한다")
  void bookSeat_success() {
    UUID reservationId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID seatId = UUID.randomUUID();
    Reservation reservation = Reservation.createTemporaryReservation(reservationId, userId, seatId, concertId, 5L);

    when(reservationRepository.findByIdAndReservationStatus(reservationId, ReservationStatus.PENDING))
        .thenReturn(Optional.of(reservation));

    ReservationRequest request = new ReservationRequest(reservationId);

    ReservationResponse response = reservationService.bookSeat(request);

    assertThat(response.getReservationStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    verify(reservationRepository).save(reservation);
  }

  @Test
  @DisplayName("예약 ID와 상태로 예약을 찾을 수 없으면 RuntimeException이 발생한다")
  void bookSeatwhenReservationNotFound() {
    UUID reservationId = UUID.randomUUID();
    when(reservationRepository.findByIdAndReservationStatus(reservationId, ReservationStatus.PENDING))
        .thenReturn(Optional.empty());

    ReservationRequest request = new ReservationRequest(reservationId);

    assertThatThrownBy(() -> reservationService.bookSeat(request))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("예약 상태가 PENDING인 예약을 모두 삭제하고 좌석 사용 가능 이벤트를 발행한다")
  void unbookTemporarySeatPublishEvents() {
    UUID reservationId = UUID.randomUUID();
    Reservation reservation = Reservation.createTemporaryReservation(
        reservationId, UUID.randomUUID(), UUID.randomUUID(), concertId, 0L
    );

    when(reservationRepository.findAllByReservationStatus(ReservationStatus.PENDING))
        .thenReturn(List.of(reservation));

    reservationService.unbookTemporarySeat();

    verify(reservationRepository).deleteAllByIdIn(List.of(reservationId));
    verify(eventPublisher).publishEvent(any(SeatAvailableStatusEvent.class));
  }
}
