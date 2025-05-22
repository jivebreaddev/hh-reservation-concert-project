package kr.hhplus.be.server.payments.application;

import kr.hhplus.be.server.common.annotation.Facade;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.event.PaymentFailureEvent;
import kr.hhplus.be.server.payments.domain.event.PaymentSuccessEvent;
import org.springframework.context.ApplicationEventPublisher;

@Facade
public class PaymentFacade {
  private final PaymentUseCase paymentUseCase;
  private final PointUseCase pointUseCase;
  private final ApplicationEventPublisher eventPublisher;

  protected PaymentFacade(PaymentUseCase paymentUseCase, PointUseCase pointUseCase,
      ApplicationEventPublisher eventPublisher) {
    this.paymentUseCase = paymentUseCase;
    this.pointUseCase = pointUseCase;
    this.eventPublisher = eventPublisher;
  }


  public ChargeResponse chargePoint(ChargeRequest chargeRequest) {
    Payment payment = paymentUseCase.sendPayment(chargeRequest.getUserId(), chargeRequest.getAmount());

    if (payment.isPaymentCompleted()){
      eventPublisher.publishEvent(new PaymentSuccessEvent(payment.getId(), payment.getAmount()));
      return pointUseCase.chargePoint(chargeRequest, payment.getId());

    }
    eventPublisher.publishEvent(new PaymentFailureEvent(payment.getId(), payment.getAmount()));
    throw new RuntimeException("Payment still in process or failed");
  }


}
