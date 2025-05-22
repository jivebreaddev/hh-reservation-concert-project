package kr.hhplus.be.server.queues.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.queues.application.dto.EnterRequest;
import kr.hhplus.be.server.queues.application.dto.EnterResponse;
import kr.hhplus.be.server.queues.application.dto.QueueRequest;
import kr.hhplus.be.server.queues.application.dto.QueueResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "대기열 API", description = "대기열/접근 가능 조회 API")
public interface IncomingQueueController {
  @Operation(summary = "예약 접근 가능 조회", description = "접근을 위해 ")
  ResponseEntity<QueueResponse> queueUser(@RequestBody QueueRequest queueRequest);

  @Operation(summary = "대기열 토큰", description = "접근 가능한지 조회합니다.")
  ResponseEntity<EnterResponse> enterUser(@RequestBody EnterRequest enterRequest);

}
