package kr.hhplus.be.server.platform.ui.listener;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import kr.hhplus.be.server.common.annotation.DomainEventListener;
import kr.hhplus.be.server.common.topic.TopicContants;
import kr.hhplus.be.server.platform.domain.DataPlatformDeadLetterQueueClient;
import kr.hhplus.be.server.platform.infra.DefaultDataPlatformClient;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@DomainEventListener
public class KafkaDataPlatformEventConsumer implements DataPlatformEventListener {

  private final DefaultDataPlatformClient client;
  private final DataPlatformDeadLetterQueueClient dlqClient;

  public KafkaDataPlatformEventConsumer(DefaultDataPlatformClient client,
      DataPlatformDeadLetterQueueClient dlqClient) {
    this.client = client;
    this.dlqClient = dlqClient;
  }


  @CircuitBreaker(name = "handleQueueEventCB")
  @KafkaListener(topics = TopicContants.SEAT_STATUS, groupId = "data-platform")
  public void handleSeatHeld(SeatHeldStatusEvent event, Acknowledgment ack) {
    try {
      client.send(event);
      ack.acknowledge();
    } catch (Exception ex) {
      dlqClient.send(event);
      ack.acknowledge();

      throw ex;
    }

  }


  @CircuitBreaker(name = "handleQueueEventCB")
  @KafkaListener(topics = TopicContants.SEAT_STATUS, groupId = "data-platform")
  public void handleCompleted(SeatPendingStatusEvent event, Acknowledgment ack) {
    try {
      client.send(event);
      ack.acknowledge();
    } catch (Exception ex) {
      dlqClient.send(event);
      ack.acknowledge();

      throw ex;
    }
  }

  @CircuitBreaker(name = "handleQueueEventCB")
  @KafkaListener(topics = TopicContants.SEAT_STATUS, groupId = "data-platform")
  public void handleSeatPending(SeatPendingStatusEvent event, Acknowledgment ack) {
    try {
      client.send(event);
      ack.acknowledge();

    } catch (Exception ex) {
      dlqClient.send(event);
      ack.acknowledge();

      throw ex;
    }
  }

  @Override
  @CircuitBreaker(name = "handleQueueEventCB")
  @KafkaListener(topics = TopicContants.SEAT_STATUS, groupId = "data-platform")
  public void handleSeatAvailable(SeatAvailableStatusEvent event, Acknowledgment ack) {
    try {
      client.send(event);
      ack.acknowledge();

    } catch (Exception ex) {
      dlqClient.send(event);
      ack.acknowledge();

      throw ex;
    }
  }

  @Override
  @CircuitBreaker(name = "handleQueueEventCB")
  @KafkaListener(topics = TopicContants.SEAT_STATUS, groupId = "data-platform")
  public void handleReservedStatus(SeatReservedStatusEvent event, Acknowledgment ack) {
    try {
      client.send(event);
      ack.acknowledge();

    } catch (Exception ex) {
      dlqClient.send(event);
      ack.acknowledge();

      throw ex;
    }
  }


}
