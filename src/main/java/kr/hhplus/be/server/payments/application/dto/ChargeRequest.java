package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "충전 요청 DTO")
public class ChargeRequest {

  private final Long userId;
  private final Long amount;

  public ChargeRequest(Long userId, Long amount) {
    this.userId = userId;
    this.amount = amount;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getAmount() {
    return amount;
  }
}
