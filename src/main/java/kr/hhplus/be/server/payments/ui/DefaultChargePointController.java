package kr.hhplus.be.server.payments.ui;

import kr.hhplus.be.server.payments.application.PaymentFacade;
import kr.hhplus.be.server.payments.application.PointUseCase;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import kr.hhplus.be.server.payments.application.dto.UseRequest;
import kr.hhplus.be.server.payments.application.dto.UseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
public class DefaultChargePointController implements ChargePointController {

  private final PaymentFacade paymentFacade;
  private final PointUseCase pointUseCase;

  protected DefaultChargePointController(PaymentFacade paymentFacade, PointUseCase pointUseCase) {
    this.paymentFacade = paymentFacade;
    this.pointUseCase = pointUseCase;
  }

  @Override
  @PostMapping("/charge")
  public ResponseEntity<ChargeResponse> chargePoint(
      @RequestBody ChargeRequest chargeRequest
  ) {
    return ResponseEntity.ok(paymentFacade.chargePoint(chargeRequest));
  }

  @Override
  @GetMapping
  public ResponseEntity<GetBalanceResponse> viewPoint(
      @RequestBody GetBalanceRequest getBalanceRequest
  ) {
    return ResponseEntity.ok(pointUseCase.getUserPoint(getBalanceRequest));
  }
  @Override
  @PostMapping("/use")
  public ResponseEntity<UseResponse> use(
      @RequestBody UseRequest useRequest
  ) {
    return ResponseEntity.ok(pointUseCase.useUserPoint(useRequest));
  }
}
