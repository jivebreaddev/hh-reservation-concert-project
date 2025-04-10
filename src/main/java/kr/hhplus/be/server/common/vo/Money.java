package kr.hhplus.be.server.common.vo;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Money {

  private Long amount;
  protected Money() {}

  public Money(Long amount) {
    if (amount == null) throw new IllegalArgumentException("amount must not be null");
    if (amount < 0) throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
    this.amount = amount;
  }

  public Money add(Money other) {
    return new Money(this.amount + other.amount);
  }

  public Money subtract(Money other) {
    return new Money(this.amount - other.amount);
  }

  public static Money of(Long amount){

    return new Money(amount);
  }
  public boolean isGreaterThan(Money other) {
    return this.amount > other.amount;
  }

  public Long getAmount() {
    return amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money money = (Money) o;
    return Objects.equals(amount, money.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }
}
