package kr.hhplus.be.server.payments.application;

import jakarta.transaction.Transactional;
import java.util.UUID;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.config.date.DateTimeFactory;
import kr.hhplus.be.server.payments.application.dto.ChargeRequest;
import kr.hhplus.be.server.payments.application.dto.ChargeResponse;
import kr.hhplus.be.server.payments.application.dto.GetBalanceRequest;
import kr.hhplus.be.server.payments.application.dto.GetBalanceResponse;
import kr.hhplus.be.server.payments.application.dto.UseRequest;
import kr.hhplus.be.server.payments.application.dto.UseResponse;
import kr.hhplus.be.server.payments.domain.Point;
import kr.hhplus.be.server.payments.domain.PointRepository;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultPointService implements PointUseCase {

  private final PointRepository pointRepository;
  private final DateTimeFactory dateTimeFactory;

  protected DefaultPointService(PointRepository pointRepository, DateTimeFactory dateTimeFactory) {
    this.pointRepository = pointRepository;
    this.dateTimeFactory = dateTimeFactory;
  }

  @Override
  @Transactional
  public ChargeResponse chargePoint(ChargeRequest chargeRequest, UUID paymentId) {
    Point point = pointRepository.findByUserId(chargeRequest.getUserId())
        .orElseThrow(IllegalArgumentException::new);

    point.chargePoint(Money.of(chargeRequest.getAmount()), dateTimeFactory.getCurrentTime());

    Point saved = pointRepository.save(point);
    return new ChargeResponse(paymentId, saved.getBalance());
  }

  @Override
  @Transactional
  public GetBalanceResponse getUserPoint(GetBalanceRequest request) {
    Point point = pointRepository.findByUserId(request.getUserId())
        .orElseThrow(IllegalArgumentException::new);

    return new GetBalanceResponse(point.getUserId(), point.getBalance());
  }

  @Override
  @Transactional
  public UseResponse useUserPoint(UseRequest request) {
    Point point = pointRepository.findByUserId(request.getUserId())
        .orElseThrow(RuntimeException::new);

    point.usePoint(Money.of(request.getAmount()), dateTimeFactory.getCurrentTime());

    Point saved = pointRepository.save(point);

    return new UseResponse(saved.getUserId(), saved.getBalance(), saved.getUpdatedAt());
  }

}
