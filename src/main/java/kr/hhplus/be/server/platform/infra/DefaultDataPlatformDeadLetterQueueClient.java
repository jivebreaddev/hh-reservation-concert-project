package kr.hhplus.be.server.platform.infra;

import kr.hhplus.be.server.common.annotation.Client;
import kr.hhplus.be.server.common.topic.TopicName;
import kr.hhplus.be.server.platform.domain.DataPlatformDeadLetterQueueClient;
import org.springframework.kafka.core.KafkaTemplate;

@Client
public class DefaultDataPlatformDeadLetterQueueClient implements DataPlatformDeadLetterQueueClient {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public DefaultDataPlatformDeadLetterQueueClient(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void send(Object event) {
    kafkaTemplate.send(TopicName.SEAT_DEAD_LETTER.value(), event)
        .thenAccept(result -> {

        })
        .exceptionally(ex -> {
          // 실패 시 처리
          // 영속화 해서 배치처리로 나중에 진행하는고 알람이 필요하다.
          return null;
        });

  }
}
