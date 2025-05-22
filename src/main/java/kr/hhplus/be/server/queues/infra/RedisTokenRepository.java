package kr.hhplus.be.server.queues.infra;


import kr.hhplus.be.server.queues.domain.Token;

import java.util.Optional;
import java.util.UUID;

public class RedisTokenRepository implements DefaultTokenRepository{

  @Override
  public Optional<Token> findByUserId(UUID userId) {
    return Optional.empty();
  }
}
