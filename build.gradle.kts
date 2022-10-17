import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
  id("org.springframework.boot") version "2.6.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.6"
  kotlin("jvm") version "1.6.10"
  kotlin("plugin.spring") version "1.6.10"
}


group = "es.adevinta"

java.sourceCompatibility = JavaVersion.VERSION_11

sourceSets{
  create("integrationTests"){
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
  }

}

val integrationTestsImplementation: Configuration by configurations.getting {
  extendsFrom(configurations.implementation.get())
  extendsFrom(configurations.testImplementation.get())
}

configurations["integrationTestsRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())
configurations["integrationTestsRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-configuration-processor")
  //jwt
  implementation("io.jsonwebtoken:jjwt-api:0.11.5")
  implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  //Databse
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.postgresql:postgresql:42.3.4")
  implementation("org.flywaydb:flyway-core:8.5.10")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.springframework.boot:spring-boot-test:2.7.0")
  testImplementation("com.github.tomakehurst:wiremock:2.27.2")
  testImplementation("io.rest-assured:spring-mock-mvc:4.5.1")
  testImplementation("org.testcontainers:junit-jupiter:1.17.2")

  //Kotli test
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("com.nhaarman:mockito-kotlin:1.6.0")
}

val integrationTests = task<Test>("integrationTests") {
  description = "Runs integration tests."
  group = "verification"

  testClassesDirs = sourceSets["integrationTests"].output.classesDirs
  classpath = sourceSets["integrationTests"].runtimeClasspath
  filter {
    includeTestsMatching("ApplicationIntegrationTest")
  }
  shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTests) }


tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"

  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events(PASSED, SKIPPED, FAILED)
    exceptionFormat = FULL
    showExceptions = true
    showCauses = true
    showStackTraces = true
  }
}
