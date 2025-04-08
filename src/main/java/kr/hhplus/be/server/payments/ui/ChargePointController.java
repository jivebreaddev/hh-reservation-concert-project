package kr.hhplus.be.server.payments.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "포인트 충전/조회", description = "포인트 관련 API")
public interface ChargePointController {
  @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
  ResponseEntity<ChargeResponse> chargePoint(@RequestBody ChargeRequest chargeRequest);

  @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
  ResponseEntity<GetBalanceResponse> viewPoint(@RequestBody GetBalanceRequest getBalanceRequest);

}
