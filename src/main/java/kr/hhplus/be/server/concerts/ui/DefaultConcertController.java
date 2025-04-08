package kr.hhplus.be.server.concerts.ui;

import java.util.List;
import kr.hhplus.be.server.concerts.application.dto.AvailableDate;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/concerts")
class DefaultConcertController implements ConcertController {

  @Override
  @GetMapping("/available")
  public ResponseEntity<GetAvailableDatesResponse> getAvailableDates(
      @RequestBody GetAvailableDatesRequest getAvailableDatesRequest) {
    return ResponseEntity.ok(new GetAvailableDatesResponse(List.of(new AvailableDate[]{})));
  }
}
