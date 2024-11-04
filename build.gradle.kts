plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
}

var temporalVersion = "1.22.3"
var javaSDKVersion = "1.22.3"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.temporal:temporal-spring-boot-starter-alpha:${temporalVersion}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	implementation("io.temporal:temporal-sdk:${javaSDKVersion}")
    testImplementation("io.temporal:temporal-testing:${javaSDKVersion}")
}