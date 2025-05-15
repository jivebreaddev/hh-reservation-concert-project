package kr.hhplus.be.server.reservations.application;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.reservations.application.dto.GetReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.GetReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.ReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.ReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationResponse;
import kr.hhplus.be.server.reservations.application.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.application.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.application.event.SeatReservedStatusEvent;
import kr.hhplus.be.server.reservations.domain.Reservation;
import kr.hhplus.be.server.reservations.domain.ReservationRepository;
import kr.hhplus.be.server.reservations.domain.ReservationStatus;
import kr.hhplus.be.server.reservations.domain.SeatClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultReservationService {

  private static final Long PENDING = 5L;
  private final ReservationRepository reservationRepository;
  private final SeatClient seatClient;
  private final ApplicationEventPublisher eventPublisher;

  protected DefaultReservationService(ReservationRepository reservationRepository,
      SeatClient seatClient, ApplicationEventPublisher eventPublisher) {
    this.reservationRepository = reservationRepository;
    this.seatClient = seatClient;
    this.eventPublisher = eventPublisher;
  }

  @Transactional(readOnly = true)
  public GetReservationResponse getReservations(
      GetReservationRequest getReservationRequest
  ) {
    List<Reservation> reservations = reservationRepository.findAllByUserId(
        getReservationRequest.getUserId());

    return GetReservationResponse.of(reservations);
  }

  @Transactional
  public TemporaryReservationResponse bookTemporarySeat(
      TemporaryReservationRequest request
  ) {
    if (seatClient.seatAvailable(request.getSeatId())) {
      eventPublisher.publishEvent(SeatHeldStatusEvent.of(request.getSeatId()));

      Reservation reservation = reservationRepository.save(
          Reservation.createTemporaryReservation(UUID.randomUUID(),
              request.getUserId(), request.getSeatId(), request.getConcertId(),
              PENDING));

      return new TemporaryReservationResponse(reservation.getUserId(), reservation.getSeatId(),
          reservation.getId(),
          reservation.getReservationStatus());
    }

    throw new RuntimeException();
  }

  @Transactional
  public ReservationResponse bookSeat(
      ReservationRequest request) {
    Reservation reservation = reservationRepository.findByIdAndReservationStatus(
            request.getReservationId(), ReservationStatus.PENDING)
        .orElseThrow(RuntimeException::new);

    reservation.createReservation();

    reservationRepository.save(reservation);

    eventPublisher.publishEvent(SeatReservedStatusEvent.of(reservation.getSeatId(), reservation.getConcertId()));

    return new ReservationResponse(reservation.getId(), reservation.getUserId(),
        reservation.getSeatId(), reservation.getReservationStatus());
  }


  @Transactional
  @Scheduled(fixedDelay = 3000L)
  public void unbookTemporarySeat() {

    List<Reservation> reservations = reservationRepository.findAllByReservationStatus(
        ReservationStatus.PENDING);
    reservationRepository.deleteAllByIdIn(
        reservations.stream()
            .map(Reservation::getId)
            .toList()
    );

    reservations.forEach(
        reservation -> eventPublisher.publishEvent(SeatAvailableStatusEvent.of(reservation.getSeatId()))
    );

  }
}
