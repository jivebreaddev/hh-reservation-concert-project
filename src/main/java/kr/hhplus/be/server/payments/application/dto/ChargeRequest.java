package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Schema(description = "충전 요청 DTO")
public class ChargeRequest {
  @NotBlank
  private final UUID userId;
  private final Long amount;

  public ChargeRequest(UUID userId, Long amount) {
    this.userId = userId;
    this.amount = amount;
  }

  public UUID getUserId() {
    return userId;
  }

  public Long getAmount() {
    return amount;
  }
}
