package kr.hhplus.be.server.queues.domain;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {

  Token save(Token token);

  Optional<Token> findById(UUID tokenId);

  Optional<Token> findByUserId(UUID userId);
}
