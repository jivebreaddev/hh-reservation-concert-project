package kr.hhplus.be.server.payments.infra;

import java.util.UUID;
import kr.hhplus.be.server.payments.domain.Payment;
import kr.hhplus.be.server.payments.domain.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultPaymentRepository extends PaymentRepository, JpaRepository<Payment, UUID> {

}
