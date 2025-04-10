package kr.hhplus.be.server.payments.infra.feign;

import kr.hhplus.be.server.payments.application.dto.PaymentRequest;
import kr.hhplus.be.server.payments.application.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-client", url = "https://api.payment.com")
public interface ExternalPaymentFeignClient {
  @PostMapping("/payments")
  PaymentResponse sendPayment(@RequestBody PaymentRequest request);
}
