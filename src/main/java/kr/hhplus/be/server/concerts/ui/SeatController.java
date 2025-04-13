package kr.hhplus.be.server.concerts.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "좌석 API", description = "예약 가능한 좌석 조회 API")
public interface SeatController {

  @Operation(summary = "좌석 조회", description = "예약 가능한 좌석을 조회합니다.")
  ResponseEntity<GetAvailableSeatsResponse> getAvailableSeats(
      @RequestBody GetAvailableSeatsRequest getAvailableSeatsRequest);

}
