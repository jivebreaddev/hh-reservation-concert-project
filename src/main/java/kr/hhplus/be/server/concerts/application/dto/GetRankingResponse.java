package kr.hhplus.be.server.concerts.application.dto;

import java.util.List;

public class GetRankingResponse {
  private final List<ConcertRankingDto> rankings;

  public GetRankingResponse(List<ConcertRankingDto> rankings) {
    this.rankings = rankings;
  }

}
