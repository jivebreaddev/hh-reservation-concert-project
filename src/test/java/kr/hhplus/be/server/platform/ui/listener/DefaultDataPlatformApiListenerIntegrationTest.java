package kr.hhplus.be.server.platform.ui.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.IntegrationTest;
import kr.hhplus.be.server.concerts.application.SeatStatusChangeListener;
import kr.hhplus.be.server.fake.FakePlatformApiListener;
import kr.hhplus.be.server.payments.domain.event.PaymentFailureEvent;
import kr.hhplus.be.server.payments.domain.event.PaymentSuccessEvent;
import kr.hhplus.be.server.queues.domain.event.QueueCompletedEvent;
import kr.hhplus.be.server.queues.domain.event.QueueEvent;
import kr.hhplus.be.server.queues.domain.event.QueueProcessingEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatAvailableStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatHeldStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatPendingStatusEvent;
import kr.hhplus.be.server.reservations.domain.event.SeatReservedStatusEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.transaction.TestTransaction;

@RecordApplicationEvents
@Transactional
class DefaultDataPlatformApiListenerIntegrationTest extends IntegrationTest {
  @MockBean
  private SeatStatusChangeListener statusChangeListener;
  @Autowired
  private FakePlatformApiListener client;
  @Autowired
  private ApplicationEvents applicationEvents;
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @BeforeEach
  void setUp() {
    client.clear();
  }
  @Test
  void testPaymentSuccessEventHandled() {
    PaymentSuccessEvent event = new PaymentSuccessEvent(UUID.randomUUID(), 100L);
    eventPublisher.publishEvent(event);

    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();

    await().atMost(5, TimeUnit.SECONDS);
    assertThat(applicationEvents.stream(PaymentSuccessEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void testPaymentFailureEventHandled() {
    PaymentFailureEvent event = new PaymentFailureEvent(UUID.randomUUID(), 100L);
    eventPublisher.publishEvent(event);
    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();
    assertThat(applicationEvents.stream(PaymentFailureEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void testSeatHeldEventHandled() {
    SeatHeldStatusEvent event = SeatHeldStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());
    eventPublisher.publishEvent(event);

    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();

    assertThat(applicationEvents.stream(SeatHeldStatusEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void testSeatAvailableEventHandled() {
    SeatAvailableStatusEvent event = SeatAvailableStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());
    eventPublisher.publishEvent(event);

    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();
    assertThat(applicationEvents.stream(SeatAvailableStatusEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void testSeatPendingEventHandled() {
    SeatPendingStatusEvent event = SeatPendingStatusEvent.of(UUID.randomUUID());
    eventPublisher.publishEvent(event);
    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();


    assertThat(applicationEvents.stream(SeatPendingStatusEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void testSeatReservedEventHandled() {
    SeatReservedStatusEvent event = SeatReservedStatusEvent.of(UUID.randomUUID(), UUID.randomUUID());
    eventPublisher.publishEvent(event);

    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();
    assertThat(applicationEvents.stream(SeatReservedStatusEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void testQueueCompletedEventHandled() {
    QueueCompletedEvent event = new QueueCompletedEvent(QueueEvent.COMPLETE, List.of());
    eventPublisher.publishEvent(event);
    if (!TestTransaction.isActive()) {
      TestTransaction.start();
    }
    TestTransaction.flagForCommit();
    TestTransaction.end();
    assertThat(applicationEvents.stream(QueueCompletedEvent.class).count()).isGreaterThanOrEqualTo(1);
    assertThat(applicationEvents.stream(QueueProcessingEvent.class).count()).isGreaterThanOrEqualTo(1);
  }
}
