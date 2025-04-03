package kr.hhplus.be.server.payment.application.dto;

public class GetBalanceRequest {

  private final Long userId;

  public GetBalanceRequest(Long userId) {
    this.userId = userId;
  }
}
