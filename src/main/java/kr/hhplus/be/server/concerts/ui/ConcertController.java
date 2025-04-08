package kr.hhplus.be.server.concerts.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "콘서트 조회 API", description = "예약 가능한 좌석 조회 API")
public interface ConcertController {

  @Operation(summary = "콘서트 예약가능 날짜 조회", description = "사용자를 생성합니다.")
  ResponseEntity<GetAvailableDatesResponse> getAvailableDates(
      GetAvailableDatesRequest getAvailableDatesRequest);

}
