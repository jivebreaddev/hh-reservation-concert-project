package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class GetBalanceResponse {
  private final UUID userId;
  private final Long balance;

  public GetBalanceResponse(UUID userId, Long balance) {
    this.userId = userId;
    this.balance = balance;
  }

  public UUID getUserId() {
    return userId;
  }

  public Long getBalance() {
    return balance;
  }
}
