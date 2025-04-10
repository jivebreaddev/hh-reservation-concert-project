package kr.hhplus.be.server.payments.domain;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

  Optional<Payment> findByUserId(UUID uuid);

  Payment save(Payment payment);
}
