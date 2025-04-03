package kr.hhplus.be.server.payment.application.dto;

public class GetBalanceResponse {
  private final Long userId;
  private final Long balance;

  public GetBalanceResponse(Long userId, Long balance) {
    this.userId = userId;
    this.balance = balance;
  }
}
