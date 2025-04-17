package kr.hhplus.be.server.fixture.payments;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentSaver {
  private final PaymentRepository paymentRepository;

  public PaymentSaver(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public Payment savePayment(Payment payment) {
    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment saveDefaultPayment(UUID userId) {
    return paymentRepository.save(PaymentFixture.createDefaultPayment(userId));
  }
}
