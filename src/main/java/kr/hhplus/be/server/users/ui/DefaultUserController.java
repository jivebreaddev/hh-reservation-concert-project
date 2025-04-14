package kr.hhplus.be.server.users.ui;

import kr.hhplus.be.server.users.application.UserFactoryService;
import kr.hhplus.be.server.users.application.dto.CreateUserRequest;
import kr.hhplus.be.server.users.application.dto.CreateUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class DefaultUserController implements UserController {

  private final UserFactoryService userFactoryService;

  public DefaultUserController(UserFactoryService userFactoryService) {
    this.userFactoryService = userFactoryService;
  }

  @Override
  @PostMapping("/create")
  public ResponseEntity<CreateUserResponse> createUser(
      @RequestBody CreateUserRequest createUserRequest) {

    return ResponseEntity.ok(
        new CreateUserResponse(userFactoryService.createUser(createUserRequest)));
  }
}
