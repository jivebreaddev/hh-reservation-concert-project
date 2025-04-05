package kr.hhplus.be.server.reservation.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.reservation.application.dto.GetReservationRequest;
import kr.hhplus.be.server.reservation.application.dto.GetReservationResponse;
import kr.hhplus.be.server.reservation.application.dto.ReservationRequest;
import kr.hhplus.be.server.reservation.application.dto.ReservationResponse;
import kr.hhplus.be.server.reservation.application.dto.TemporaryReservationRequest;
import kr.hhplus.be.server.reservation.application.dto.TemporaryReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "콘서트 예약 API", description = "콘서트 예약 API")
public interface ReserveSeatController {

  @Operation(summary = "좌석 임시 예약 API", description = "좌석을 임시 예약 합니다.")
  ResponseEntity<TemporaryReservationResponse> reserveSeatTemporarily(
      @RequestHeader("pass-token") String passToken,
      @RequestBody TemporaryReservationRequest queueRequest);

  @Operation(summary = "좌석 예약 API", description = "좌석을 예약 합니다.")
  ResponseEntity<ReservationResponse> reserveSeat(@RequestHeader("pass-token") String passToken,
      @RequestBody ReservationRequest queueRequest);

  @Operation(summary = "예약 조회 API", description = "좌석 예약을 조회합니다.")
  ResponseEntity<GetReservationResponse> getReservations(
      @RequestHeader("pass-token") String passToken,
      @RequestBody GetReservationRequest queueRequest);
}
