package kr.hhplus.be.server.queues.infra;

import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;

public class RedisTokenRepository implements TokenRepository {


  @Override
  public Token save(Token token) {
    return null;
  }

  @Override
  public Optional<Token> findById(UUID tokenId) {
    return Optional.empty();
  }

  @Override
  public Optional<Token> findByUserId(UUID userId) {
    return Optional.empty();
  }
}
