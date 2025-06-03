package kr.hhplus.be.server.platform.ui.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.UUID;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.common.topic.TopicContants;
import kr.hhplus.be.server.platform.domain.DataPlatformDeadLetterQueueClient;
import kr.hhplus.be.server.platform.infra.DefaultDataPlatformClient;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;

class KafkaDataPlatformEventConsumerIntegrationTest extends IntegrationTest {

  @Autowired
  KafkaTemplate<String, Object> kafkaTemplate;

  @Autowired
  KafkaDataPlatformEventConsumer consumer;

  @MockBean
  DefaultDataPlatformClient mockClient;

  @MockBean
  DataPlatformDeadLetterQueueClient mockDlqClient;

  @Test
  @DisplayName("handleSeatHeld 정상 처리 테스트")
  void testHandleSeatHeld_success() throws Exception {
    SeatHeldStatusEvent event = SeatHeldStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> verify(mockClient, atLeastOnce()).send(event));

    verify(mockDlqClient, never()).send(any());
  }

  @Test
  @DisplayName("handleSeatHeld 실패 시 DLQ 전송 테스트")
  void testHandleSeatHeld_failure() throws Exception {
    SeatHeldStatusEvent event = SeatHeldStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    doThrow(new RuntimeException("Send failed")).when(mockClient).send(event);

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> {
          verify(mockClient, atLeastOnce()).send(event);
          verify(mockDlqClient, atLeastOnce()).send(event);
        });
  }

  @Test
  @DisplayName("handleSeatAvailable 정상 처리 테스트")
  void testHandleSeatAvailable_success() throws Exception {
    SeatAvailableStatusEvent event = SeatAvailableStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> verify(mockClient, atLeastOnce()).send(event));

    verify(mockDlqClient, never()).send(any());
  }

  @Test
  @DisplayName("handleSeatAvailable 실패 시 DLQ 전송 테스트")
  void testHandleSeatAvailable_failure() throws Exception {
    SeatAvailableStatusEvent event = SeatAvailableStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    doThrow(new RuntimeException("Send failed")).when(mockClient).send(event);

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> {
          verify(mockClient, atLeastOnce()).send(event);
          verify(mockDlqClient, atLeastOnce()).send(event);
        });
  }

  @Test
  @DisplayName("handleCompleted 정상 처리 테스트")
  void testHandleCompleted_success() throws Exception {
    SeatReservedStatusEvent event = SeatReservedStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> verify(mockClient, atLeastOnce()).send(event));

    verify(mockDlqClient, never()).send(any());
  }

  @Test
  @DisplayName("handleCompleted 실패 시 DLQ 전송 테스트")
  void testHandleCompleted_failure() throws Exception {
    SeatReservedStatusEvent event = SeatReservedStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    doThrow(new RuntimeException("Send failed")).when(mockClient).send(event);

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> {
          verify(mockClient, atLeastOnce()).send(event);
          verify(mockDlqClient, atLeastOnce()).send(event);
        });
  }

  @Test
  @DisplayName("handleSeatPending 정상 처리 테스트")
  void testHandleSeatPending_success() throws Exception {
    SeatPendingStatusEvent event = SeatPendingStatusEvent.of(UUID.randomUUID());

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> verify(mockClient, atLeastOnce()).send(event));

    verify(mockDlqClient, never()).send(any());
  }

  @Test
  @DisplayName("handleSeatPending 실패 시 DLQ 전송 테스트")
  void testHandleSeatPending_failure() throws Exception {
    SeatPendingStatusEvent event = SeatPendingStatusEvent.of(UUID.randomUUID());

    doThrow(new RuntimeException("Send failed")).when(mockClient).send(event);

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> {
          verify(mockClient, atLeastOnce()).send(event);
          verify(mockDlqClient, atLeastOnce()).send(event);
        });
  }

  @Test
  @DisplayName("handleReservedStatus 정상 처리 테스트")
  void testHandleReservedStatus_success() throws Exception {
    SeatReservedStatusEvent event = SeatReservedStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> verify(mockClient, atLeastOnce()).send(event));

    verify(mockDlqClient, never()).send(any());
  }

  @Test
  @DisplayName("handleReservedStatus 실패 시 DLQ 전송 테스트")
  void testHandleReservedStatus_failure() throws Exception {
    SeatReservedStatusEvent event = SeatReservedStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());

    doThrow(new RuntimeException("Send failed")).when(mockClient).send(event);

    kafkaTemplate.send(TopicContants.SEAT_STATUS, event.getSeatId().toString(), event).get();

    Awaitility.await().atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> {
          verify(mockClient, atLeastOnce()).send(event);
          verify(mockDlqClient, atLeastOnce()).send(event);
        });
  }

}
