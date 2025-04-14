package kr.hhplus.be.server.queues.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import kr.hhplus.be.server.queues.application.dto.EnterRequest;
import kr.hhplus.be.server.queues.application.dto.EnterResponse;
import kr.hhplus.be.server.queues.application.dto.QueueRequest;
import kr.hhplus.be.server.queues.application.dto.QueueResponse;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.queues.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

class DefaultQueueFacadeTest {
  @Mock
  private QueueFactoryUseCase queueFactoryUseCase;

  @Mock
  private TokenFactoryUseCase tokenFactoryUseCase;

  @InjectMocks
  private QueueFacade queueFacade;

  @Test
  @DisplayName("사용자를 큐에 등록하고 토큰을 생성하면 응답에 포함됩니다.")
  void queueUser() {
    // GIVEN
    UUID userId = UUID.randomUUID();
    UUID queueId = UUID.randomUUID();
    UUID tokenId = UUID.randomUUID();

    Queue queue = mock(Queue.class);
    when(queue.getQueueStatus()).thenReturn(QueueStatus.WAITING);
    when(queue.getId()).thenReturn(queueId);

    Token token = mock(Token.class);
    when(token.getId()).thenReturn(tokenId);

    when(queueFactoryUseCase.createQueue(userId)).thenReturn(queue);
    when(tokenFactoryUseCase.createToken(userId, queueId)).thenReturn(token);

    QueueRequest request = new QueueRequest(userId);

    // WHEN
    QueueResponse response = queueFacade.queueUser(request);

    // THEN
    assertThat(response.getUserId()).isEqualTo(userId);
    assertThat(response.getQueueStatus()).isEqualTo(QueueStatus.WAITING);
    assertThat(response.getToken()).isEqualTo(tokenId);
  }

  @Test
  @DisplayName("사용자의 큐 상태를 조회할 수 있습니다.")
  void getQueueStatus() {
    // GIVEN
    UUID userId = UUID.randomUUID();

    Queue queue = mock(Queue.class);
    when(queue.getQueueStatus()).thenReturn(QueueStatus.PROCESSING);

    when(queueFactoryUseCase.getQueue(userId)).thenReturn(queue);

    EnterRequest request = new EnterRequest(userId);

    // WHEN
    EnterResponse response = queueFacade.getQueueStatus(request);

    // THEN
    assertThat(response.getUserId()).isEqualTo(userId);
    assertThat(response.getQueueStatus()).isEqualTo(QueueStatus.PROCESSING);
  }
}

