package kr.hhplus.be.server.payments.application.dto;

import java.util.UUID;

public class PaymentRequest {
  private UUID userId;
  private Long amount;

  public PaymentRequest(UUID userId, Long amount) {
    this.userId = userId;
    this.amount = amount;
  }

  public PaymentRequest() {
  }

  public UUID getUserId() {
    return userId;
  }

  public Long getAmount() {
    return amount;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }
}
