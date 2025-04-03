package kr.hhplus.be.server.user.application.dto;

public class CreateUserRequest {
  private final Long userId;

  public CreateUserRequest(Long userId) {
    this.userId = userId;
  }
}
