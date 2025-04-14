package kr.hhplus.be.server.queues.ui;

import kr.hhplus.be.server.queues.application.QueueFacade;
import kr.hhplus.be.server.queues.application.dto.EnterRequest;
import kr.hhplus.be.server.queues.application.dto.EnterResponse;
import kr.hhplus.be.server.queues.application.dto.QueueRequest;
import kr.hhplus.be.server.queues.application.dto.QueueResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queues")
public class DefaultIncomingQueueController implements IncomingQueueController {

  private final QueueFacade queueFacade;

  public DefaultIncomingQueueController(QueueFacade queueFacade) {
    this.queueFacade = queueFacade;
  }

  @Override
  @PostMapping("/queue")
  public ResponseEntity<QueueResponse> queueUser(@RequestBody QueueRequest queueRequest) {
    QueueResponse response = queueFacade.queueUser(queueRequest);

    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/entering")
  public ResponseEntity<EnterResponse> enterUser(@RequestBody EnterRequest enterRequest) {
    EnterResponse response = queueFacade.getQueueStatus(enterRequest);

    return ResponseEntity.ok(response);
  }
}
