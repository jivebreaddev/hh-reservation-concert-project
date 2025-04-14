package kr.hhplus.be.server.users.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.users.application.dto.CreateUserRequest;
import kr.hhplus.be.server.users.application.dto.CreateUserResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "유저 API", description = "유저 생성 API")
public interface UserController {

  @Operation(summary = "사용자 생성", description = "사용자를 생성합니다.")
  ResponseEntity<CreateUserResponse> createUser(CreateUserRequest createUserRequest);
}
