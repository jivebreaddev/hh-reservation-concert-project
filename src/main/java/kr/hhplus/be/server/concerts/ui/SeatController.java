package kr.hhplus.be.server.concerts.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "좌석 조회 API", description = "예약 가능한 좌석 조회 API")
public interface SeatController {

  @Operation(summary = "사용자 생성", description = "사용자를 생성합니다.")
  ResponseEntity<GetAvailableDatesResponse> getAvailableSeats(
      GetAvailableDatesRequest getAvailableDatesRequest);

}
