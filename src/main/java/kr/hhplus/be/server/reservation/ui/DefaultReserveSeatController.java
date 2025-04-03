package kr.hhplus.be.server.reservation.ui;

import java.time.LocalDateTime;
import kr.hhplus.be.server.reservation.application.dto.GetReservationRequest;
import kr.hhplus.be.server.reservation.application.dto.GetReservationResponse;
import kr.hhplus.be.server.reservation.application.dto.ReservationRequest;
import kr.hhplus.be.server.reservation.application.dto.ReservationResponse;
import kr.hhplus.be.server.reservation.application.dto.TemporaryReservationRequest;
import kr.hhplus.be.server.reservation.application.dto.TemporaryReservationResponse;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/reservation")

public class DefaultReserveSeatController implements ReserveSeatController {

  @Override
  @PostMapping("/reserve-temporarily")
  public ResponseEntity<TemporaryReservationResponse> reserveSeatTemporarily(String passToken,
      TemporaryReservationRequest queueRequest) {
    return ResponseEntity.ok(new TemporaryReservationResponse(0L, 0L, ReservationStatus.CONFIRMED));
  }

  @Override
  @PostMapping("/reserve")
  public ResponseEntity<ReservationResponse> reserveSeat(String passToken,
      ReservationRequest queueRequest) {
    return ResponseEntity.ok(new ReservationResponse(0L, 0L, ReservationStatus.CONFIRMED));
  }

  @Override
  @GetMapping
  public ResponseEntity<GetReservationResponse> getReservations(String passToken,
      GetReservationRequest queueRequest) {
    return ResponseEntity.ok(new GetReservationResponse(LocalDateTime.now(), 0L, "A", 0L, 0L, "IU",
        ReservationStatus.CONFIRMED));
  }
}
