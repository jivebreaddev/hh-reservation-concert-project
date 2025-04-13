package kr.hhplus.be.server.payments.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentClient;
import kr.hhplus.be.server.payments.domain.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultPaymentServiceTest {

  @Mock
  private PaymentClient paymentClient;
  @Mock

  private DefaultPaymentStore paymentStore;
  @InjectMocks
  private DefaultPaymentService paymentService;

  @Nested
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  class 회원의_포인트_값을_조회_하는_경우 {

    @Test
    @DisplayName("[성공] 결제를 성공하여 저장까지 완료한다.")
    void getUserPoint() {
      // given
      UUID paymentId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      Long amount = 1000L;

      Payment payment = Payment.of(paymentId, userId, amount, PaymentStatus.SUCCESS);
      Payment savedPayment = Payment.of(paymentId, userId, amount, PaymentStatus.SUCCESS);

      when(paymentClient.sendPayment(userId, amount)).thenReturn(payment);
      when(paymentStore.save(payment)).thenReturn(savedPayment);

      // when
      Payment result = paymentService.sendPayment(userId, amount);

      // then
      assertThat(result).isEqualTo(savedPayment);
      verify(paymentClient, times(1)).sendPayment(userId, amount);
      verify(paymentStore, times(1)).save(payment);
    }


    @Test
    @DisplayName("[실패] 결제를 실패하지만 실패 결제의 저장은 성공한다.")
    void sendFailPaymentsButSucceedStore() {
      // given
      UUID userId = UUID.randomUUID();
      Long amount = 1000L;

      // 결제 실패 응답
      Payment failedPayment = Payment.of(UUID.randomUUID(), userId, amount, PaymentStatus.FAILED);

      // 결제 클라이언트는 실패 응답을 처리해 PaymentStatus.FAILED 반환
      when(paymentClient.sendPayment(userId, amount)).thenReturn(failedPayment);

      // 저장은 성공함
      when(paymentStore.save(failedPayment)).thenReturn(failedPayment);

      // when
      Payment result = paymentService.sendPayment(userId, amount);

      // then
      assertThat(result.isPaymentCompleted()).isNotEqualTo(PaymentStatus.FAILED);
      verify(paymentClient, times(1)).sendPayment(userId, amount);
      verify(paymentStore, times(1)).save(failedPayment);
    }

    @Test
    @DisplayName("[실패] 결제를 성공하지만 저장은 실패한다.")
    void sendSuccessPaymentStoreFails() {
      // given
      UUID userId = UUID.randomUUID();
      Long amount = 1000L;
      Payment payment = Payment.of(UUID.randomUUID(), userId, amount, PaymentStatus.SUCCESS);

      when(paymentClient.sendPayment(userId, amount)).thenReturn(payment);
      when(paymentStore.save(payment)).thenThrow(new RuntimeException("DB 저장 실패"));

      // when & then
      RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        paymentService.sendPayment(userId, amount);
      });

      verify(paymentClient, times(1)).sendPayment(userId, amount);
      verify(paymentStore, times(1)).save(payment);
    }
  }

}
