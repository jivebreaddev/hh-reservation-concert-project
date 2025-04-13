package kr.hhplus.be.server.payments.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PaymentTest {

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 결제_상태값_체크 {

    @Test
    @DisplayName("결제 상태에 따라, 결제 완료인지 반환합니다.")
    void isPaymentCompleted() {
      // WHEN
      Payment payment = Payment.of(UUID.randomUUID(), UUID.randomUUID(), 1000L,
          PaymentStatus.SUCCESS);
      Payment inProgressPayment = Payment.of(UUID.randomUUID(), UUID.randomUUID(), 1000L,
          PaymentStatus.PENDING);

      // THEN
      assertThat(payment.isPaymentCompleted()).isEqualTo(true);
      assertThat(inProgressPayment.isPaymentCompleted()).isEqualTo(false);
    }
  }

}
