package kr.hhplus.be.server.common.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public class Money {

  private Long amount;
  protected Money() {}

  public Money(Long amount) {
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


}
