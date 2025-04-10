package kr.hhplus.be.server.reservations.ui;

import kr.hhplus.be.server.reservations.application.DefaultReservationService;
import kr.hhplus.be.server.reservations.application.dto.GetReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.GetReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.ReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.ReservationResponse;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationRequest;
import kr.hhplus.be.server.reservations.application.dto.TemporaryReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/reservations"))
public class DefaultReserveSeatController implements ReserveSeatController {

  private final DefaultReservationService reservationService;

  public DefaultReserveSeatController(DefaultReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @Override
  @PostMapping("/reserve-temporarily")
  public ResponseEntity<TemporaryReservationResponse> reserveSeatTemporarily(
      @RequestHeader("pass-token") String passToken,
      @RequestBody TemporaryReservationRequest request) {
    return ResponseEntity.ok(reservationService.bookTemporarySeat(request));
  }

  @Override
  @PostMapping("/reserve")
  public ResponseEntity<ReservationResponse> reserveSeat(
      @RequestHeader("pass-token") String passToken,
      @RequestBody ReservationRequest request) {
    return ResponseEntity.ok(reservationService.bookSeat(request));
  }

  @Override
  @GetMapping
  public ResponseEntity<GetReservationResponse> getReservations(
      @RequestHeader("/pass-token") String passToken,
      @RequestBody GetReservationRequest request) {
    return ResponseEntity.ok(reservationService.getReservations(request));

  }
}
