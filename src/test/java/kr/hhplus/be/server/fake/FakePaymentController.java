package kr.hhplus.be.server.fake;


import java.util.UUID;
import kr.hhplus.be.server.payments.application.dto.PaymentRequest;
import kr.hhplus.be.server.payments.application.dto.PaymentResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Profile("test")
public class FakePaymentController {
  @PostMapping("/payments")
  public PaymentResponse getPaymentStatus(@RequestBody PaymentRequest request) {
    if (request.getAmount() == 999L) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "의도적인 실패");
    }

    return new PaymentResponse(UUID.randomUUID(), "SUCCESS");
  }

}
