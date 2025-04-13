package kr.hhplus.be.server.queues.infra;

import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultTokenRepository extends TokenRepository, JpaRepository<UUID, Token> {

}
