package kr.hhplus.be.server.queues.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {

  List<Token> findByExpiredDateAfter(LocalDateTime now);

  void deleteAllIdsIn(List<UUID> list);

  Token save(Token token);

  Optional<Token> findById(UUID tokenId);

  Optional<Token> findByUserId(UUID userId);
}
