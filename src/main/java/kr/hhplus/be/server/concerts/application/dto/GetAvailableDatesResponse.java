package kr.hhplus.be.server.concerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.concerts.domain.ConcertSchedules;

@Schema
public class GetAvailableDatesResponse {

  private final List<AvailableDate> availableDates;

  public GetAvailableDatesResponse(List<AvailableDate> availableDates) {
    this.availableDates = availableDates;
  }

  public static GetAvailableDatesResponse of(List<ConcertSchedules> concertSchedules) {
    return new GetAvailableDatesResponse(
        concertSchedules.stream()
            .map(AvailableDate::from)
            .toList());
  }
}
