package kr.hhplus.be.server.queues.application;

import static org.junit.jupiter.api.Assertions.*;

import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.queues.domain.QueueStatus;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QueueStateValidatorTest extends IntegrationTest {
  @Autowired
  private QueueStateValidator validator;

  @Test
  @DisplayName("유효한 상태 변화1 ")
  void validTransition_waitingToProcessing() {
    // given
    QueueStatus currentStatus = QueueStatus.WAITING;
    QueueEvent event = QueueEvent.PROCESS;

    // when
    boolean result = validator.isValidTransition(currentStatus, event);

    // then
    assertTrue(result);
  }

  @Test
  @DisplayName("유효한 상태 변화2 ")
  void validTransition_processingToDone() {
    // given
    QueueStatus currentStatus = QueueStatus.PROCESSING;
    QueueEvent event = QueueEvent.COMPLETE;

    // when
    boolean result = validator.isValidTransition(currentStatus, event);

    // then
    assertTrue(result);
  }

  @Test
  @DisplayName("유효하지않은 상태 변화 ")
  void invalidTransition_waitingToDone() {
    // given
    QueueStatus currentStatus = QueueStatus.WAITING;
    QueueEvent event = QueueEvent.COMPLETE;

    // when
    boolean result = validator.isValidTransition(currentStatus, event);

    // then
    assertFalse(result);
  }
}
