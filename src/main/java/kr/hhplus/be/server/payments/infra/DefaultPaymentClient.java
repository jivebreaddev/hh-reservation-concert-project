package kr.hhplus.be.server.payments.infra;

import java.util.UUID;
import kr.hhplus.be.server.payments.application.dto.PaymentRequest;
import kr.hhplus.be.server.payments.application.dto.PaymentResponse;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentClient;
import kr.hhplus.be.server.payments.domain.PaymentStatus;
import kr.hhplus.be.server.payments.infra.feign.ExternalPaymentFeignClient;
import org.springframework.stereotype.Component;

@Component
public class DefaultPaymentClient implements PaymentClient {

  private final ExternalPaymentFeignClient externalPaymentFeignClient;

  public DefaultPaymentClient(ExternalPaymentFeignClient externalPaymentFeignClient) {
    this.externalPaymentFeignClient = externalPaymentFeignClient;
  }

  @Override
  public Payment sendPayment(UUID userId, Long amount) {
    try {
      PaymentResponse response = externalPaymentFeignClient.sendPayment(
          new PaymentRequest(userId, amount));

      return Payment.of(
          response.getPaymentId(),
          userId,
          amount,
          PaymentStatus.valueOf(response.getStatus())
      );
    } catch (Exception e) {
      return Payment.of(
          UUID.randomUUID(),
          userId,
          amount,
          PaymentStatus.FAILED
      );
    }
  }
}
