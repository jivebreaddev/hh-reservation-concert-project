package kr.hhplus.be.server.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class CreateUserRequest {
  private final Long userId;

  public CreateUserRequest(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
