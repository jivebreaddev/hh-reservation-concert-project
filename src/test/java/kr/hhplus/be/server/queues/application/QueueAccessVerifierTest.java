package kr.hhplus.be.server.queues.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.queues.domain.Queue;
import kr.hhplus.be.server.queues.domain.QueueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueueAccessVerifierTest {
  @Mock
  private QueueRepository queueRepository;
  @InjectMocks
  private QueueAccessVerifier queueAccessVerifier;

  @Test
  @DisplayName("token 있을때, 접근이 가능하다.")
  void accessWithToken() {
    // given
    UUID userId = UUID.randomUUID();
    Queue mockQueue = mock(Queue.class);
    when(queueRepository.findByUserId(userId)).thenReturn(Optional.of(mockQueue));
    when(mockQueue.isProcessing()).thenReturn(true);

    // when
    boolean result = queueAccessVerifier.canAccess(userId.toString());

    // then
    assertTrue(result);
    verify(queueRepository, times(1)).findByUserId(userId);
  }

  @Test
  @DisplayName("token 없으면 오류를 표기합니다.")
  void withoutTokenNotAllowed() {
    UUID userId = UUID.randomUUID();
    Queue queue = mock(Queue.class);
    when(queue.isProcessing()).thenReturn(false);
    when(queueRepository.findByUserId(userId)).thenReturn(Optional.of(queue));

    // when
    boolean result = queueAccessVerifier.canAccess(userId.toString());

    // then
    assertFalse(result);
    verify(queueRepository).findByUserId(userId);
  }
}
