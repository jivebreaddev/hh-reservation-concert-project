package kr.hhplus.be.server;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class ConcertReservationSimulation extends Simulation {

    /*
    성능 테스트 기준

    | 항목                     | 요구 조건                                |
    |--------------------------|--------------------------------------|
    | **활성 토큰 수 (VU)**     | 100                                  |
    | **QPS (Queries Per Second)** | 20 ~ 50                           |
    | **처리량 (Throughput)**   | 초당 최소 50 요청 이상                       |
    | **응답 시간 (Response Time)** | p95: 200ms 이하<br>p99: 300 ~ 400ms 이하 |
    | **오류율 (Error Rate)**    | 0.1% 이하
    */

  HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8080")
      .contentTypeHeader("application/json");
  List<String> reservationIds = Stream.generate(() -> UUID.randomUUID().toString())
      .limit(1000000)
      .collect(Collectors.toList());

  List<Map<String, Object>> data = reservationIds.stream()
      .map(id -> {
        Map<String, Object> map = new HashMap<>();
        map.put("reservation_id", id);
        return map;
      })
      .collect(Collectors.toList());
  FeederBuilder<Object> feeder = listFeeder(data);


  ScenarioBuilder reserveScenario = scenario("콘서트 예약")
      .feed(feeder)
      .exec(http("POST /api/v1/reservations/reserve")
          .post("/api/v1/reservations/reserve")
          .body(StringBody(session ->
              String.format("{\"reservation_id\": \"%s\"}", session.getString("reservation_id"))
          )).asJson()
          .check(status().is(200)));

  OpenInjectionStep[] injectionSteps = new OpenInjectionStep[]{
      rampUsersPerSec(20).to(50).during(Duration.ofSeconds(30)),   // 20 QPS → 50 QPS 30초
      constantUsersPerSec(50).during(Duration.ofSeconds(30)),      // 50 QPS 30초 유지
      rampUsersPerSec(50).to(20).during(Duration.ofSeconds(30))    // 50 QPS → 20 QPS 30초 감소
  };

  {
    setUp(
        reserveScenario.injectOpen(injectionSteps).protocols(httpProtocol)
    )
        .maxDuration(Duration.ofMinutes(1))  // 총 60초 테스트
        .assertions(
            global().requestsPerSec().gte(50.0),                // 처리량 최소 50 QPS 이상
            global().responseTime().percentile(95).lte(200),    // p95 응답시간 200ms 이하
            global().responseTime().percentile(99).lte(400),    // p99 응답시간 400ms 이하
            global().failedRequests().percent().lte(0.1)        // 오류율 0.1% 이하
        );
  }
}
