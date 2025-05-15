package kr.hhplus.be.server.concerts.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.concerts.application.ConcertQueryUseCase;
import kr.hhplus.be.server.concerts.application.RankingUseCase;
import kr.hhplus.be.server.concerts.application.dto.AvailableDate;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesRequest;
import kr.hhplus.be.server.concerts.application.dto.GetAvailableDatesResponse;
import kr.hhplus.be.server.concerts.application.dto.GetRankingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/concerts")
class DefaultConcertController implements ConcertController {

  private final ConcertQueryUseCase concertQueryUseCase;
  private final RankingUseCase rankingUseCase;

  DefaultConcertController(ConcertQueryUseCase concertQueryUseCase, RankingUseCase rankingUseCase) {
    this.concertQueryUseCase = concertQueryUseCase;
    this.rankingUseCase = rankingUseCase;
  }

  @Override
  @GetMapping("/available")
  public ResponseEntity<GetAvailableDatesResponse> getAvailableDates(
      @RequestBody GetAvailableDatesRequest getAvailableDatesRequest) {
    UUID id = UUID.fromString(getAvailableDatesRequest.getConcertId());
    return ResponseEntity.ok(concertQueryUseCase.getAvailableDatesResponseList(id));
  }

  @Override
  @GetMapping("/rankings")
  public ResponseEntity<GetRankingResponse> getConcertRankings() {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    return ResponseEntity.ok(new GetRankingResponse(rankingUseCase.getTopNConcerts(today)));
  }
}
