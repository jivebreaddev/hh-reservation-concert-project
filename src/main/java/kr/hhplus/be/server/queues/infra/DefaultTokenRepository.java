package kr.hhplus.be.server.queues.infra;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface DefaultTokenRepository extends JpaRepository<Token, UUID> {

  List<Token> findByExpiredAtAfter(LocalDateTime now);

  void deleteAllByIdIn(List<UUID> list);
  Optional<Token> findByUserId(UUID userId);
}
