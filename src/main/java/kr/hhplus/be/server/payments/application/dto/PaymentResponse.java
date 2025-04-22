package kr.hhplus.be.server.payments.application.dto;


import java.util.UUID;

public class PaymentResponse {
  private UUID paymentId;
  private String status;

  public PaymentResponse(UUID paymentId, String status) {
    this.paymentId = paymentId;
    this.status = status;
  }

  public PaymentResponse() {
  }

  public UUID getPaymentId() {
    return paymentId;
  }

  public String getStatus() {
    return status;
  }

  public void setPaymentId(UUID paymentId) {
    this.paymentId = paymentId;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
