package kr.hhplus.be.server.seat.application.dto;

public class AvailableSeat {
  private final Long lineId;
  private final String sectionId;
  private final Long seatId;

  public AvailableSeat(Long lineId, String sectionId, Long seatId) {
    this.lineId = lineId;
    this.sectionId = sectionId;
    this.seatId = seatId;
  }
}
