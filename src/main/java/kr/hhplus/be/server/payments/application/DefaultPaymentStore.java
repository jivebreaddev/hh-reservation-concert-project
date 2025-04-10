package kr.hhplus.be.server.payments.application;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentStore {
  private final PaymentRepository paymentRepository;


  public DefaultPaymentStore(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public Payment save(Payment payment){
    return paymentRepository.save(payment);
  };
}
