package kr.hhplus.be.server.seat.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class AvailableSeat {
  private final Long lineId;
  private final String sectionId;
  private final Long seatId;

  public AvailableSeat(Long lineId, String sectionId, Long seatId) {
    this.lineId = lineId;
    this.sectionId = sectionId;
    this.seatId = seatId;
  }

  public Long getLineId() {
    return lineId;
  }

  public String getSectionId() {
    return sectionId;
  }

  public Long getSeatId() {
    return seatId;
  }
}
