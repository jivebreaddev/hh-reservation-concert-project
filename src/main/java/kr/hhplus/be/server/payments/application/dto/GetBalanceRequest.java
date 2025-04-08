package kr.hhplus.be.server.payments.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class GetBalanceRequest {

  private final Long userId;

  public GetBalanceRequest(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
