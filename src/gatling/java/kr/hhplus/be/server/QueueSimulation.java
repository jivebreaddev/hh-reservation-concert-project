package kr.hhplus.be.server;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.listFeeder;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.nothingFor;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueueSimulation extends Simulation {

  HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8080")
      .shareConnections()
      .contentTypeHeader("application/json")
      .maxConnectionsPerHost(10000);

  List<String> userIds = Stream.generate(() -> UUID.randomUUID().toString())
      .limit(1000000)
      .collect(Collectors.toList());

  List<Map<String, Object>> data = userIds.stream()
      .map(id -> {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        return map;
      })
      .collect(Collectors.toList());

  FeederBuilder<Object> feeder = listFeeder(data);

  ScenarioBuilder queueTokenScenario = scenario("Queue Token 생성 및 인입")
      .feed(feeder)
      .exec(http("POST /api/v1/queues/queue")
          .post("/api/v1/queues/queue")
          .body(StringBody(session ->
              String.format("{\"user_id\": \"%s\"}", session.getString("user_id"))
          )).asJson()
          .check(status().is(200)))
      .exitHereIfFailed();

  ScenarioBuilder tokenStatusScenario = scenario("토큰 발급 상태 조회")
      .feed(feeder)
      .exec(http("GET /api/v1/queues/entering")
          .get("/api/v1/queues/entering")
          .body(StringBody(session ->
              String.format("{\"user_id\": \"%s\"}", session.getString("user_id"))
          )).asJson()
          .check(status().is(200)))
      .exitHereIfFailed();

  OpenInjectionStep[] injectionSteps = new OpenInjectionStep[]{
      rampUsersPerSec(0).to(1000).during(Duration.ofSeconds(30))

  };

  {
    setUp(
        queueTokenScenario.injectOpen(injectionSteps).protocols(httpProtocol),
                tokenStatusScenario
                    .injectOpen(
                        openInjectionStepsForTokenStatus()
                    )
                    .protocols(httpProtocol)
    ).maxDuration(Duration.ofMinutes(2));
//        .assertions(
//            global().requestsPerSec().gte(50.0),
//            global().successfulRequests().percent().gte(99.9)
//        );
  }

  private OpenInjectionStep[] openInjectionStepsForTokenStatus() {
    // 2000 QPS에서 1분간 100 QPS씩 감소시키는 로직
    // QPS 감소는 2000 -> 1900 -> 1800 이런 식
    return new OpenInjectionStep[]{
        nothingFor(Duration.ofSeconds(15)),
        rampUsersPerSec(1000).to(900).during(Duration.ofSeconds(12)),
        rampUsersPerSec(900).to(800).during(Duration.ofSeconds(12)),
        rampUsersPerSec(800).to(700).during(Duration.ofSeconds(12)),
        rampUsersPerSec(700).to(600).during(Duration.ofSeconds(12)),
        constantUsersPerSec(600).during(Duration.ofSeconds(12))  // 마지막 구간 유지
    };
  }
}
