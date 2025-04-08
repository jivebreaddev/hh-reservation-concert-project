package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class GetBalanceResponse {
  private final Long userId;
  private final Long balance;

  public GetBalanceResponse(Long userId, Long balance) {
    this.userId = userId;
    this.balance = balance;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getBalance() {
    return balance;
  }
}
