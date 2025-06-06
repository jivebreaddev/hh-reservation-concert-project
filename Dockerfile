FROM amazoncorretto:21	 AS builder

ENV GRADLE_USER_HOME=/gradle-cache

COPY src ./src
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle ./gradle
RUN chmod 755 ./gradlew

RUN ./gradlew dependencies

RUN ./gradlew build -x test --warning-mode=all --parallel

FROM amazoncorretto:21
ARG JAR_FILES=build/libs/server-*.jar
COPY --from=builder ${JAR_FILES} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
