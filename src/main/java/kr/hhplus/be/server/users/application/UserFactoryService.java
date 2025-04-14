package kr.hhplus.be.server.users.application;

import java.util.UUID;
import kr.hhplus.be.server.common.annotation.Facade;
import kr.hhplus.be.server.users.application.dto.CreateUserRequest;
import kr.hhplus.be.server.users.domain.User;
import kr.hhplus.be.server.users.domain.UserRepository;

@Facade
public class UserFactoryService {

  private final UserRepository userRepository;

  public UserFactoryService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UUID createUser(CreateUserRequest createUserRequest) {

    User user = userRepository.save(
        User.of(createUserRequest.getName(), createUserRequest.getEmail()));

    return user.getId();
  }
}
