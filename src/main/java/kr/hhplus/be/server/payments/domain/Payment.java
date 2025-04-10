package kr.hhplus.be.server.payments.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private UUID id;

  @Column(name = "user_id", columnDefinition = "binary(16)")
  private UUID userId;

  @Column(name = "amount", nullable = false, columnDefinition = "BIGINT")
  private Long amount;

  @Column(name = "status", nullable = false, columnDefinition = "varchar(255)")
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  private Payment(UUID id, UUID userId, Long amount, PaymentStatus paymentStatus) {
    this.id = id;
    this.userId = userId;
    this.amount = amount;
    this.paymentStatus = paymentStatus;
  }


  public static Payment of(UUID id, UUID userId, Long amount, PaymentStatus paymentStatus){
    return new Payment(id, userId, amount, paymentStatus);
  }

  public boolean isPaymentCompleted(){
    return paymentStatus.equals(PaymentStatus.SUCCESS);
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Payment payment = (Payment) o;
    return Objects.equals(id, payment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
