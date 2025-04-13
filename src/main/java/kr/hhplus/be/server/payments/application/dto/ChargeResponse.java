package kr.hhplus.be.server.payments.application.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.hhplus.be.server.payments.domain.PaymentStatus;

@Schema
public class ChargeResponse {
  private final UUID paymentId;
  private final Long balance;

  public ChargeResponse(UUID paymentId, Long balance) {
    this.paymentId = paymentId;
    this.balance = balance;
  }

  public UUID getPaymentId() {
    return paymentId;
  }

  public Long getBalance() {
    return balance;
  }

}
