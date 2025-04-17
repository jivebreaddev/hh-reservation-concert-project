package kr.hhplus.be.server.users.infra;

import java.util.UUID;
import kr.hhplus.be.server.users.domain.User;
import kr.hhplus.be.server.users.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DefaultUserRepository extends UserRepository, JpaRepository<User, UUID> {

}
