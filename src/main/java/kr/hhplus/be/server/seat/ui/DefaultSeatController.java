package kr.hhplus.be.server.seat.ui;

import java.util.List;
import kr.hhplus.be.server.seat.application.dto.AvailableSeat;
import kr.hhplus.be.server.seat.application.dto.AvailableSeats;
import kr.hhplus.be.server.seat.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.seat.application.dto.GetAvailableDatesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/seat")

public class DefaultSeatController implements SeatController {

  @Override
  @GetMapping
  public ResponseEntity<GetAvailableDatesResponse> getAvailableSeats(
      GetAvailableDatesRequest getAvailableDatesRequest) {
    return ResponseEntity.ok(new GetAvailableDatesResponse(new AvailableSeats(
        List.of(new AvailableSeat(0L, "A", 0L)))));
  }
}
