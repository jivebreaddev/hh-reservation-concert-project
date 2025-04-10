package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.domain.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {

  @Mock
  private PaymentUseCase paymentUseCase;

  @Mock
  private PointUseCase pointUseCase;

  @InjectMocks
  private PaymentFacade paymentFacade;

  @Test
  @DisplayName("[성공] 결제가 성공할때, 충전이 성공한다.")
  void chargePoint_success() {
    // given
    UUID userId = UUID.randomUUID();
    Long amount = 1000L;
    UUID paymentId = UUID.randomUUID();
    ChargeRequest request = new ChargeRequest(userId, amount);

    Payment payment = mock(Payment.class);
    when(payment.isPaymentCompleted()).thenReturn(true);
    when(payment.getId()).thenReturn(paymentId);

    ChargeResponse expectedResponse = new ChargeResponse(paymentId, amount);

    when(paymentUseCase.sendPayment(userId, amount)).thenReturn(payment);
    when(pointUseCase.chargePoint(request, paymentId)).thenReturn(expectedResponse);

    // when
    ChargeResponse result = paymentFacade.chargePoint(request);

    // then
    assertThat(result).isEqualTo(expectedResponse);

    verify(paymentUseCase).sendPayment(userId, amount);
    verify(pointUseCase).chargePoint(request, paymentId);
  }

  @Test
  @DisplayName("[실패] 결제가 실패할때, 충전이 실패한다.")
  void paymentFailWillThrowError() {
    // given
    UUID userId = UUID.randomUUID();
    Long amount = 1000L;
    ChargeRequest request = new ChargeRequest(userId, amount);

    Payment payment = mock(Payment.class);
    when(payment.isPaymentCompleted()).thenReturn(false);

    when(paymentUseCase.sendPayment(userId, amount)).thenReturn(payment);

    // when & then
    assertThatThrownBy(() -> paymentFacade.chargePoint(request))
        .isInstanceOf(RuntimeException.class);

    verify(paymentUseCase).sendPayment(userId, amount);
    verify(pointUseCase, never()).chargePoint(any(), any());
  }
}
