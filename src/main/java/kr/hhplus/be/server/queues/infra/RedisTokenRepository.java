package kr.hhplus.be.server.queues.infra;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;

public class RedisTokenRepository implements DefaultTokenRepository{


  @Override
  public Optional<Token> findByUserId(UUID userId) {
    return Optional.empty();
  }
}
