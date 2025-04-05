package kr.hhplus.be.server.queue.ui;

import kr.hhplus.be.server.queue.application.dto.EnterRequest;
import kr.hhplus.be.server.queue.application.dto.EnterResponse;
import kr.hhplus.be.server.queue.application.dto.QueueRequest;
import kr.hhplus.be.server.queue.application.dto.QueueResponse;
import kr.hhplus.be.server.queue.domain.QueueStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
public class DefaultIncomingQueueController implements IncomingQueueController {

  @Override
  @PostMapping("/queue")
  public ResponseEntity<QueueResponse> queueUser(@RequestBody QueueRequest queueRequest) {
    return ResponseEntity.ok(new QueueResponse(0L, QueueStatus.WAITING));
  }

  @Override
  @GetMapping("/entering")
  public ResponseEntity<EnterResponse> enterUser(@RequestBody EnterRequest enterRequest) {
    return ResponseEntity.ok(new EnterResponse(0L, 15L, QueueStatus.PROCESSING));
  }
}
