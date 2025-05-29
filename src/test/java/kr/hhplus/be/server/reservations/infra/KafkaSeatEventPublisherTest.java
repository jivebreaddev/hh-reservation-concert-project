package kr.hhplus.be.server.reservations.infra;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import kr.hhplus.be.server.common.topic.TopicName;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class KafkaSeatEventPublisherTest {

  @Mock
  private KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  private KafkaSeatEventPublisher publisher;

  @BeforeEach
  void setup() {
    CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
    future.complete(mock(SendResult.class));

    when(kafkaTemplate.send(anyString(), any(), any())).thenReturn(future);
  }

  @Test
  @DisplayName("testPublishSeatReservedEvent 정상 호출 시 kafkaTemplate.send 호출")
  void testPublishSeatReservedEvent() {
    SeatReservedStatusEvent event = mock(SeatReservedStatusEvent.class);
    UUID seatId = UUID.randomUUID();
    when(event.getSeatId()).thenReturn(seatId);

    publisher.publishSeatReservedEvent(event);

    verify(kafkaTemplate, times(1))
        .send(eq(TopicName.SEAT_STATUS.value()), eq(seatId.toString()), eq(event));
  }

  @Test
  @DisplayName("testPublishSeatPendingEvent 정상 호출 시 kafkaTemplate.send 호출")
  void testPublishSeatPendingEvent() {
    SeatPendingStatusEvent event = mock(SeatPendingStatusEvent.class);
    UUID seatId = UUID.randomUUID();

    when(event.getSeatId()).thenReturn(seatId);

    publisher.publishSeatPendingEvent(event);

    verify(kafkaTemplate, times(1))
        .send(eq(TopicName.SEAT_STATUS.value()), eq(seatId.toString()), eq(event));
  }

  @Test
  @DisplayName("testPublishSeatAvailableEvent 정상 호출 시 kafkaTemplate.send 호출")
  void testPublishSeatAvailableEvent() {
    SeatAvailableStatusEvent event = mock(SeatAvailableStatusEvent.class);
    UUID seatId = UUID.randomUUID();
    when(event.getSeatId()).thenReturn(seatId);

    publisher.publishSeatAvailableEvent(event);

    verify(kafkaTemplate, times(1))
        .send(eq(TopicName.SEAT_STATUS.value()), eq(seatId.toString()), eq(event));
  }
}
