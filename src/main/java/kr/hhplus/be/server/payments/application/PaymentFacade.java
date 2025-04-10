package kr.hhplus.be.server.payments.application;

import kr.hhplus.be.server.common.annotation.Facade;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.domain.Payment;

@Facade
public class PaymentFacade {
  private final PaymentUseCase paymentUseCase;
  private final PointUseCase pointUseCase;

  protected PaymentFacade(PaymentUseCase paymentUseCase, PointUseCase pointUseCase) {
    this.paymentUseCase = paymentUseCase;
    this.pointUseCase = pointUseCase;
  }


  public ChargeResponse chargePoint(ChargeRequest chargeRequest) {
    Payment payment = paymentUseCase.sendPayment(chargeRequest.getUserId(), chargeRequest.getAmount());

    if (payment.isPaymentCompleted()){
      return pointUseCase.chargePoint(chargeRequest, payment.getId());
    }

    throw new RuntimeException("Payment still in process or failed");
  }


}
