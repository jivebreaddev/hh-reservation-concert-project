package kr.hhplus.be.server.payments.application;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentClient;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentService implements PaymentUseCase {

  private final PaymentClient paymentClient;
  private final DefaultPaymentStore paymentStore;

  protected DefaultPaymentService(PaymentClient paymentClient,
      DefaultPaymentStore paymentStore) {
    this.paymentClient = paymentClient;
    this.paymentStore = paymentStore;
  }

  @Override
  public Payment sendPayment(UUID userId, Long amount) {
    Payment payment = paymentClient.sendPayment(userId, amount);
    try {
      // Transaction 시작
      Payment saved = paymentStore.save(payment);
      return saved;
    } catch (Exception e) {
      // payment에 실패한다면, 보상처리가 필요
      throw e;
    }
  }

}
