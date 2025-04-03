package kr.hhplus.be.server.payment.application.dto;


import kr.hhplus.be.server.payment.domain.PaymentStatus;

public class ChargeResponse {
  private final Long paymentId;

  private final Long balance;
  private final PaymentStatus paymentStatus;

  public ChargeResponse(Long paymentId, Long balance, PaymentStatus paymentStatus) {
    this.paymentId = paymentId;
    this.balance = balance;
    this.paymentStatus = paymentStatus;
  }
}
