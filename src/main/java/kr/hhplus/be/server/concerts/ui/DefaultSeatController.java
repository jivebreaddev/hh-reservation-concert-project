package kr.hhplus.be.server.concerts.ui;

import kr.hhplus.be.server.concerts.application.SeatQueryUseCase;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableSeatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seats")
class DefaultSeatController implements SeatController {

  private final SeatQueryUseCase seatQueryUseCase;

  DefaultSeatController(SeatQueryUseCase seatQueryUseCase) {
    this.seatQueryUseCase = seatQueryUseCase;
  }

  @Override
  @GetMapping("/available")
  public ResponseEntity<GetAvailableSeatsResponse> getAvailableSeats(
      @RequestBody GetAvailableSeatsRequest getAvailableSeatsRequest) {
    return ResponseEntity.ok(
        seatQueryUseCase.getAvailableSeatsResponseList(getAvailableSeatsRequest));
  }

}
