package kr.hhplus.be.server.reservations.infra;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import io.github.resilience4j.retry.annotation.Retry;
import kr.hhplus.be.server.common.topic.TopicName;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatEventPublisher;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaSeatEventPublisher implements SeatEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public KafkaSeatEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Retry(name = "handleQueueEventRetry")
  public void publishSeatReservedEvent(SeatReservedStatusEvent event) {
    kafkaTemplate.send(TopicName.SEAT_STATUS.value(), event.getSeatId().toString(), event)
        .thenAccept(result -> {

        })
        .exceptionally(ex -> {
          // 실패 시 처리
          // 영속화 해서 배치처리로 나중에 진행하는고 알람이 필요하다.
          return null;
        });
    ;
  }

  @Override
  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Retry(name = "handleQueueEventRetry")
  public void publishSeatPendingEvent(SeatPendingStatusEvent event) {
    kafkaTemplate.send(TopicName.SEAT_STATUS.value(), event.getSeatId().toString(), event)
        .thenAccept(result -> {

        })
        .exceptionally(ex -> {
          // 실패 시 처리
          // 영속화 해서 배치처리로 나중에 진행하는고 알람이 필요하다.
          return null;
        });
    ;
  }

  @Override
  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Retry(name = "handleQueueEventRetry")
  public void publishSeatAvailableEvent(SeatAvailableStatusEvent event) {
    kafkaTemplate.send(TopicName.SEAT_STATUS.value(), event.getSeatId().toString(), event)
        .thenAccept(result -> {

        })
        .exceptionally(ex -> {
          // 실패 시 처리
          // 영속화 해서 배치처리로 나중에 진행하는고 알람이 필요하다.
          return null;
        });
    ;
  }

  @Override
  @TransactionalEventListener(phase = AFTER_COMMIT)
  @Retry(name = "handleQueueEventRetry")
  public void publishSeatHeldEvent(SeatHeldStatusEvent event) {
    kafkaTemplate.send(TopicName.SEAT_STATUS.value(), event.getSeatId().toString(), event)
        .thenAccept(result -> {

        })
        .exceptionally(ex -> {
          // 실패 시 처리
          // 영속화 해서 배치처리로 나중에 진행하는고 알람이 필요하다.
          return null;
        });
    ;
  }
}
