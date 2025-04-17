package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.TestcontainersConfiguration;
import kr.hhplus.be.server.config.jpa.JpaConfig;
import kr.hhplus.be.server.fixture.payments.PaymentSaver;
import kr.hhplus.be.server.fixture.payments.PointSaver;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
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
public class PaymentFacadeIntegrationTest {

  @Autowired
  DatabaseCleanup databaseCleanup;
  @Autowired
  PointSaver pointSaver;
  @Autowired
  PaymentSaver paymentSaver;
  @Autowired
  private PointRepository pointRepository;

  @Autowired
  private PaymentFacade paymentFacade;
  private final UUID userId = UUID.randomUUID();
  @BeforeEach
  void cleanDatabase() {
    databaseCleanup.cleanUp(List.of("payments"));
    Point initialPoint = Point.of(userId, 0L);
    pointRepository.save(initialPoint);
  }

  @Test
  @DisplayName("성공한 결제 후 포인트 충전")
  void chargePointAfterPaymentSuccess() {
    ChargeRequest request = new ChargeRequest(userId, 1000L);
    ChargeResponse response = paymentFacade.chargePoint(request);

    assertThat(response.getBalance()).isEqualTo(1000L);
  }

  @Test
  @DisplayName("성공한 결제 후 포인트 충전")
  void chargePointAfterPaymentFailure() {
    ChargeRequest request = new ChargeRequest(userId, 999L);

    assertThatThrownBy(() -> paymentFacade.chargePoint(request))
        .isInstanceOf(RuntimeException.class);
  }

}
