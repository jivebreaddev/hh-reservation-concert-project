package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "충전 요청 DTO")
public class UseRequest {
  @NotBlank
  private final UUID userId;
  private final Long amount;
  private final LocalDateTime updatedTime;

  public UseRequest(UUID userId, Long amount, LocalDateTime updatedTime) {
    this.userId = userId;
    this.amount = amount;
    this.updatedTime = updatedTime;
  }

  public UUID getUserId() {
    return userId;
  }

  public Long getAmount() {
    return amount;
  }
}
