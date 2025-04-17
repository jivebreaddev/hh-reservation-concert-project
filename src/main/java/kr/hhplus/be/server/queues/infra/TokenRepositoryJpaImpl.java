package kr.hhplus.be.server.queues.infra;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepositoryJpaImpl implements TokenRepository {

  private final DefaultTokenRepository defaultTokenRepository;

  protected TokenRepositoryJpaImpl(DefaultTokenRepository defaultTokenRepository) {
    this.defaultTokenRepository = defaultTokenRepository;
  }

  @Override
  public List<Token> findByExpiredDateAfter(LocalDateTime now) {
    return defaultTokenRepository.findByExpiredAtAfter(now);
  }

  @Override
  public void deleteAllIdsIn(List<UUID> list) {
    defaultTokenRepository.deleteAllByIdIn(list);

  }

  @Override
  public Token save(Token token) {
    return defaultTokenRepository.save(token);
  }

  @Override
  public Optional<Token> findById(UUID tokenId) {
    return defaultTokenRepository.findById(tokenId);
  }

  @Override
  public Optional<Token> findByUserId(UUID userId) {
    return defaultTokenRepository.findByUserId(userId);
  }
}
