package kr.hhplus.be.server.payments.domain;

import java.util.UUID;

public interface PaymentClient {
  Payment sendPayment(UUID userId, Long payment);
}
