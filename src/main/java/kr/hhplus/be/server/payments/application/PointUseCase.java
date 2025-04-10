package kr.hhplus.be.server.payments.application;

import java.util.UUID;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import kr.hhplus.be.server.payments.application.dto.UseRequest;
import kr.hhplus.be.server.payments.application.dto.UseResponse;

public interface PointUseCase {

  ChargeResponse chargePoint(ChargeRequest chargeRequest, UUID paymentId);

  GetBalanceResponse getUserPoint(GetBalanceRequest request);

  UseResponse useUserPoint(UseRequest request);
}
