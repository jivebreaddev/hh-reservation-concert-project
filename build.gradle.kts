import io.gatling.gradle.GatlingRunTask

plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.gatling.gradle") version "3.13.5"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
    // Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	// redis
	implementation("org.redisson:redisson-spring-boot-starter:3.46.0")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	// DB
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.statemachine:spring-statemachine-core:3.2.0")

	implementation ("io.github.resilience4j:resilience4j-spring-boot3")
	implementation ("io.github.resilience4j:resilience4j-all")
	implementation("org.apache.kafka:kafka-clients:3.8.0")

	// Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.statemachine:spring-statemachine-test:3.2.0")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testImplementation("p6spy:p6spy:3.9.1")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


	gatlingImplementation("io.gatling:gatling-core:3.13.5")
	gatlingImplementation("io.gatling:gatling-http:3.13.5")
	gatlingImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.13.5")
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}


gatling {
	jvmArgs = listOf("-server", "-Xms512M", "-Xmx512M")
	systemProperties = mapOf("file.encoding" to "UTF-8")
}

sourceSets["gatling"].java.srcDir("src/gatling/java")
sourceSets["gatling"].resources.srcDir("src/gatling/resources")

tasks.withType<GatlingRunTask> {
	jvmArgs = listOf(
			"--add-opens", "java.base/java.lang=ALL-UNNAMED",
			"--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED"
	)
}
