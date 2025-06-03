package kr.hhplus.be.server.platform.ui.listener;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import kr.hhplus.be.server.platform.domain.DataPlatformDeadLetterQueueClient;
import kr.hhplus.be.server.platform.infra.DefaultDataPlatformClient;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;


@ExtendWith(MockitoExtension.class)
public class KafkaDataPlatformEventConsumerTest {

  @Mock
  private DefaultDataPlatformClient client;

  @Mock
  private DataPlatformDeadLetterQueueClient dlqClient;

  @InjectMocks
  private KafkaDataPlatformEventConsumer consumer;



  @Test
  @DisplayName("testHandleSeatHeld 정상 호출 시 client.send 호출")
  void testHandleSeatHeld() {
    SeatHeldStatusEvent event = mock(SeatHeldStatusEvent.class);
    Acknowledgment acknowledgment = mock(Acknowledgment.class);

    consumer.handleSeatHeld(event, acknowledgment);

    verify(client, times(1)).send(event);
    verifyNoInteractions(dlqClient);
  }

  @Test
  @DisplayName("testHandleSeatHeld 실패 시 dlqClient.send 호출 후 예외 던짐")
  void testHandleSeatHeld_WithException() {
    SeatHeldStatusEvent event = mock(SeatHeldStatusEvent.class);
    Acknowledgment acknowledgment = mock(Acknowledgment.class);

    doThrow(new RuntimeException("fail")).when(client).send(event);

    try {
      consumer.handleSeatHeld(event, acknowledgment);
    } catch (Exception ignored) {
    }

    verify(client, times(1)).send(event);
    verify(dlqClient, times(1)).send(event);
    verify(acknowledgment, times(1)).acknowledge();

  }

}
