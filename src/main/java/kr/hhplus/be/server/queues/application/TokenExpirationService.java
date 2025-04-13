package kr.hhplus.be.server.queues.application;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.queues.domain.Token;
import kr.hhplus.be.server.queues.domain.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenExpirationService implements TokenExpirationUseCase {

  private final TokenRepository tokenRepository;

  protected TokenExpirationService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }


  @Override
  public void expireToken() {
    List<Token> tokens = tokenRepository.findByExpiredDateAfter(LocalDateTime.now());

    tokenRepository.deleteAllIdsIn(tokens.stream()
        .map(token -> token.getId()).toList());

  }
}
