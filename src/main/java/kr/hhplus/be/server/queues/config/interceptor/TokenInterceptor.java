package kr.hhplus.be.server.queues.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import kr.hhplus.be.server.queues.application.QueueAccessVerifier;
import kr.hhplus.be.server.queues.application.QueueFactoryUseCase;
import kr.hhplus.be.server.queues.application.TokenFactoryUseCase;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.queues.domain.Token;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class TokenInterceptor implements HandlerInterceptor {

  private final QueueAccessVerifier queueAccessVerifier;

  public TokenInterceptor(QueueAccessVerifier queueAccessVerifier) {
    this.queueAccessVerifier = queueAccessVerifier;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String tokenId = request.getHeader("pass-token");

    if (tokenId == null || tokenId.isBlank()) {
      throw new RuntimeException("토큰이 존재하지 않습니다.");
    }
    if (queueAccessVerifier.canAccess(tokenId)){
      return true;
    }

    return false;
  }
}
