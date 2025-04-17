package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.TestcontainersConfiguration;
import kr.hhplus.be.server.config.jpa.JpaConfig;
import kr.hhplus.be.server.fixture.payments.PaymentSaver;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.infra.DefaultPaymentRepository;
import kr.hhplus.be.server.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8081")
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, JpaConfig.class})
public class DefaultPaymentServiceIntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  PaymentSaver saver;
  @Autowired
  DefaultPaymentService defaultPaymentService;

  @Autowired
  DefaultPaymentRepository repository;

  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of());
  }
  @DisplayName("결제 요청 성공 시 상태가 COMPLETE로 저장되어야 한다.")
  @Test
  void testPaymentStatusCheck() {
    // Given
    UUID userId = UUID.randomUUID();

    // WHEN
    Payment payment = defaultPaymentService.sendPayment(userId, 1000L);
    Payment saved = repository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException());
    // THEN
    assertThat(payment.isPaymentCompleted()).isTrue();
    assertThat(saved.getAmount()).isEqualTo(1000L);
  }

  @DisplayName("결제 요청 실패 시 상태가 FAILED로 저장되어야한다.")
  @Test
  void testFailedPaymentRequest() {
    // Given
    UUID userId = UUID.randomUUID();
    // When
    Payment payment = defaultPaymentService.sendPayment(userId, 999L);
    Payment saved = repository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException());
    // Then
    assertThat(payment.isPaymentCompleted()).isFalse();
    assertThat(saved.getAmount()).isEqualTo(999L);

  }

}
