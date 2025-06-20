package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Schema
public class GetBalanceRequest {
  @NotBlank
  private final UUID userId;

  public GetBalanceRequest(UUID userId) {
    this.userId = userId;
  }

  public UUID getUserId() {
    return userId;
  }
}
