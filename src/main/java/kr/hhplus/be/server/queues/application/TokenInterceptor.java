package kr.hhplus.be.server.queues.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.queues.domain.Token;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class TokenInterceptor implements HandlerInterceptor {

  private final QueueFactoryUseCase queueFactoryUseCase;
  private final TokenFactoryUseCase tokenFactoryUseCase;

  public TokenInterceptor(QueueFactoryUseCase queueFactoryUseCase,
      TokenFactoryUseCase tokenFactoryUseCase) {
    this.queueFactoryUseCase = queueFactoryUseCase;
    this.tokenFactoryUseCase = tokenFactoryUseCase;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String tokenId = request.getHeader("pass-token");

    if (tokenId == null || tokenId.isBlank()) {
      throw new RuntimeException("토큰이 존재하지 않습니다.");
    }
    Token token = tokenFactoryUseCase.getToken(UUID.fromString(tokenId));
    QueueStatus queueStatus = queueFactoryUseCase.getQueue(token.getUserId()).getQueueStatus();
    return queueStatus == QueueStatus.PROCESSING;
  }
}
