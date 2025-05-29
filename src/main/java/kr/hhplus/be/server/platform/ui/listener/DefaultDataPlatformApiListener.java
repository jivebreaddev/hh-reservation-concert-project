package kr.hhplus.be.server.platform.ui.listener;


import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import kr.hhplus.be.server.payments.domain.event.PaymentFailureEvent;
import kr.hhplus.be.server.payments.domain.event.PaymentSuccessEvent;
import kr.hhplus.be.server.platform.infra.DefaultDataPlatformClient;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Deprecated
@Component
public class DefaultDataPlatformApiListener {

  private final DefaultDataPlatformClient client;

  public DefaultDataPlatformApiListener(DefaultDataPlatformClient client) {
    this.client = client;
  }

  @Async
  @Bulkhead(name = "handleQueueEventBulkhead", type = Bulkhead.Type.THREADPOOL)
  @CircuitBreaker(name = "handleQueueEventCB")
  @Retry(name = "handleQueueEventRetry")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handlePaymentFailure(PaymentFailureEvent event) {
    client.send(event);
  }

  @Async
  @Bulkhead(name = "handleQueueEventBulkhead", type = Bulkhead.Type.THREADPOOL)
  @CircuitBreaker(name = "handleQueueEventCB")
  @Retry(name = "handleQueueEventRetry")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handlePaymentSuccess(PaymentSuccessEvent event) {
    client.send(event);
  }


  @Async
  @Bulkhead(name = "handleQueueEventBulkhead", type = Bulkhead.Type.THREADPOOL)
  @CircuitBreaker(name = "handleQueueEventCB")
  @Retry(name = "handleQueueEventRetry")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleQueueCompleted(QueueCompletedEvent event) {
    client.send(event);
  }

}
