package kr.hhplus.be.server.payments.ui;

import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import kr.hhplus.be.server.payments.domain.PaymentStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/points")
public class DefaultChargePointController implements ChargePointController {

  @Override
  @PostMapping("/charge")
  public ResponseEntity<ChargeResponse> chargePoint(@RequestBody ChargeRequest chargeRequest) {
    return ResponseEntity.ok(new ChargeResponse(0L, 0L, PaymentStatus.SUCCESS));
  }

  @Override
  @GetMapping
  public ResponseEntity<GetBalanceResponse> viewPoint(@RequestBody GetBalanceRequest getBalanceRequest) {
    return ResponseEntity.ok(new GetBalanceResponse(0L, 0L));
  }
}
