package kr.hhplus.be.server.payments.domain.event;

import java.util.UUID;

public class PaymentSuccessEvent {
  private final UUID paymentId;
  private final Long amount;

  public PaymentSuccessEvent(UUID id, Long amount) {
    this.paymentId = id;
    this.amount = amount;
  }
}
