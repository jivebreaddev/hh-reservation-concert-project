package kr.hhplus.be.server.payment.application.dto;

public class ChargeRequest {

  private final Long userId;
  private final Long amount;

  public ChargeRequest(Long userId, Long amount) {
    this.userId = userId;
    this.amount = amount;
  }

}
