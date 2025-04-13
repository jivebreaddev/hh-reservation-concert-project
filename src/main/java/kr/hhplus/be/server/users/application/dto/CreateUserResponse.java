package kr.hhplus.be.server.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema
public class CreateUserResponse {

  private final UUID userId;

  public CreateUserResponse(UUID userId) {
    this.userId = userId;

  }

  public UUID getUserId() {
    return userId;
  }
}
