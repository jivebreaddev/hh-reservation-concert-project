package kr.hhplus.be.server.queues.ui.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import kr.hhplus.be.server.queues.application.QueueStateService;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultProcessingEventListenerTest {

  @Mock
  private QueueStateService queueStateService;
  @InjectMocks
  private DefaultProcessingEventListener listener;

  @Test
  @DisplayName("HandleProcessingEvent 호출시 service호출")
  void HandleProcessingEvent() {
    QueueProcessingEvent event = mock(QueueProcessingEvent.class);

    listener.handleProcessing(event);

    verify(queueStateService, times(1)).handleProcessingEvent(event);
  }

  @Test
  @DisplayName("HandleCompletedEvent 호출시 service호출")
  void handleCompletedEvent() throws Exception {
    QueueCompletedEvent event = mock(QueueCompletedEvent.class);

    listener.handleCompleted(event);

    verify(queueStateService, times(1)).handleCompletedEvent(event);
  }


}
