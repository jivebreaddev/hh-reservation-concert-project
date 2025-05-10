package kr.hhplus.be.server.payments.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import kr.hhplus.be.server.common.vo.Money;

@Table(name = "points")
@Entity
public class Point {

  @Id
  @Column(name = "id", columnDefinition = "binary(16)")
  private UUID id;
  @Column(name = "user_id", columnDefinition = "binary(16)")
  private UUID userId;
  @Column(name = "balance", nullable = false, columnDefinition = "BIGINT")
  private Money balance;
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  protected Point(UUID id, UUID userId, Money balance, LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.balance = balance;
    this.updatedAt = updatedAt;
  }

  protected Point() {
  }

  public static Point of(UUID userId, Long balance){

    return new Point(UUID.randomUUID(), userId, Money.of(balance), LocalDateTime.now());
  }

  public Money chargePoint(Money money, LocalDateTime updateTime) {
    this.updatedAt = updateTime;
    return this.balance = this.balance.add(money);
  }

  public Money usePoint(Money money, LocalDateTime updateTime) {
    this.updatedAt = updateTime;
    return this.balance = this.balance.subtract(money);
  }

  public Long getBalance() {
    return balance.getAmount();
  }
  public UUID getUserId() {
    return userId;
  }
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Point point = (Point) o;
    return Objects.equals(id, point.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
