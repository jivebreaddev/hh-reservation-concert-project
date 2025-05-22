package kr.hhplus.be.server.payments.domain.event;

import java.util.UUID;

public class PaymentFailureEvent {
  private final UUID paymentId;
  private final Long amount;

  public PaymentFailureEvent(UUID id, Long amount) {
    this.paymentId = id;
    this.amount = amount;
  }
}
