package kr.hhplus.be.server.payments.application.dto;

import java.util.UUID;

public class PaymentRequest {
  private UUID userId;
  private Long amount;

  public PaymentRequest(UUID userId, Long amount) {
    this.userId = userId;
    this.amount = amount;
  }
}
