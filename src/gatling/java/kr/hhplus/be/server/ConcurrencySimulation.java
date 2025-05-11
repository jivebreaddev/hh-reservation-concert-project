package kr.hhplus.be.server;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ConcurrencySimulation extends Simulation {
  // HTTP 프로토콜 설정 (기본 URL, 헤더 등)
  HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080") // 테스트할 서버 URL
      .acceptHeader("application/json");

  // 시나리오 설정
  ScenarioBuilder scn = scenario("Basic Load Test")
      .exec(http("GET Request") // 요청 이름
          .get("/api/health") // 실제 테스트할 엔드포인트
          .check(status().is(200))); // 응답 상태가 200인지 확인

  {
    // 시나리오 실행 방식
    setUp(
        scn.injectOpen(atOnceUsers(10)) // 동시 10명 요청
    ).protocols(httpProtocol);
  }
}
