package kr.hhplus.be.server.payment.application.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.payment.domain.PaymentStatus;

@Schema
public class ChargeResponse {
  private final Long paymentId;

  private final Long balance;
  private final PaymentStatus paymentStatus;

  public ChargeResponse(Long paymentId, Long balance, PaymentStatus paymentStatus) {
    this.paymentId = paymentId;
    this.balance = balance;
    this.paymentStatus = paymentStatus;
  }

  public Long getPaymentId() {
    return paymentId;
  }

  public Long getBalance() {
    return balance;
  }

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }
}
