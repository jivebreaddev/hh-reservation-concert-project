package kr.hhplus.be.server.user.ui;

import kr.hhplus.be.server.user.application.dto.CreateUserRequest;
import kr.hhplus.be.server.user.application.dto.CreateUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class DefaultUserController implements UserController {

  @Override
  @PostMapping("/create")
  public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
    return ResponseEntity.ok(new CreateUserResponse(10L));
  }
}
