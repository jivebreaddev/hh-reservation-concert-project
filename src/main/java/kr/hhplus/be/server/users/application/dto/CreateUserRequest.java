package kr.hhplus.be.server.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class CreateUserRequest {

  private final String name;
  private final String email;

  public CreateUserRequest(Long userId, String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}
