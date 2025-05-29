package kr.hhplus.be.server.reservations.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

class KafkaSeatEventPublisherIntegrationTest extends IntegrationTest {

  @Autowired
  KafkaTemplate<String, Object> kafkaTemplate;

  @Autowired
  KafkaSeatEventPublisher kafkaSeatEventPublisher;


  private LinkedBlockingQueue<ConsumerRecord<String, Object>> records;

  @Test
  @DisplayName("SeatPendingStatusEvent 발생 시 Kafka 메시지가 정상 발행된다")
  void publishSeatPendingEvent_shouldSendMessage() throws InterruptedException {
    SeatReservedStatusEvent event = SeatReservedStatusEvent.of(UUID.randomUUID(),
        UUID.randomUUID());
    kafkaSeatEventPublisher.publishSeatReservedEvent(event);
    ConsumerRecord<String, Object> record = records.poll(5, TimeUnit.SECONDS);
    assertThat(record).isNotNull();
    assertThat(record.value()).isInstanceOf(SeatReservedStatusEvent.class);
    assertThat(((SeatReservedStatusEvent) record.value()).getSeatId()).isEqualTo(event.getSeatId());
  }

  @Test
  @DisplayName("publishSeatAvailableEvent 정상 발행 및 수신 테스트")
  void publishSeatAvailableEventTest() throws InterruptedException {
    SeatAvailableStatusEvent event = SeatAvailableStatusEvent.of(UUID.randomUUID(),
        UUID.randomUUID());
    kafkaSeatEventPublisher.publishSeatAvailableEvent(event);

    ConsumerRecord<String, Object> record = records.poll(5, TimeUnit.SECONDS);
    assertThat(record).isNotNull();
    assertThat(record.value()).isInstanceOf(SeatAvailableStatusEvent.class);
    assertThat(((SeatAvailableStatusEvent) record.value()).getSeatId()).isEqualTo(
        event.getSeatId());
  }

  @Test
  @DisplayName("publishSeatHeldEvent 정상 발행 및 수신 테스트")
  void publishSeatHeldEventTest() throws InterruptedException {
    SeatHeldStatusEvent event = SeatHeldStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());
    kafkaSeatEventPublisher.publishSeatHeldEvent(event);

    ConsumerRecord<String, Object> record = records.poll(5, TimeUnit.SECONDS);
    assertThat(record).isNotNull();
    assertThat(record.value()).isInstanceOf(SeatHeldStatusEvent.class);
    assertThat(((SeatHeldStatusEvent) record.value()).getSeatId()).isEqualTo(event.getSeatId());
  }

}
