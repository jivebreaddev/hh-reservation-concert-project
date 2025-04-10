package kr.hhplus.be.server.payments.application;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Payment;

public interface PaymentUseCase {

  Payment sendPayment(UUID userId, Long amount);

}
