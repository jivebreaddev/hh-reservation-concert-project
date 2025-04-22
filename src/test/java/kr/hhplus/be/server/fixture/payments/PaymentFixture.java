package kr.hhplus.be.server.fixture.payments;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentStatus;

public class PaymentFixture {
  public static Payment createPayment(UUID userId, Long amount) {
    return Payment.of(
        UUID.randomUUID(),
        userId,
        amount,
        PaymentStatus.SUCCESS
    );
  }

  public static Payment createPendingPayment(UUID userId, Long amount) {
    return Payment.of(
        UUID.randomUUID(),
        userId,
        amount,
        PaymentStatus.PENDING
    );
  }

  public static Payment createFailedPayment(UUID userId, Long amount) {
    return Payment.of(
        UUID.randomUUID(),
        userId,
        amount,
        PaymentStatus.FAILED
    );
  }

  public static Payment createDefaultPayment(UUID userId) {
    return createPayment(userId, 100L);  // 기본 금액 100
  }
}
