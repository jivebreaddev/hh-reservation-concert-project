package kr.hhplus.be.server.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class CreateUserResponse {
  private final Long userId;

  public CreateUserResponse(Long userId) {
    this.userId = userId;

  }

  public Long getUserId() {
    return userId;
  }
}
