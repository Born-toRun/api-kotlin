plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("org.springframework.boot")
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
//    id("org.asciidoctor.convert") version "2.4.0"
}

noArg {
    annotation("javax.persistence.Entity")
    invokeInitializers = true
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

group = "kr.kro.btr"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val snippetsDir = file("build/generated-snippets")
val asciidoctorExt: Configuration by configurations.creating

ext {
    set("snippetsDir", snippetsDir)
}

configurations {
    all {
        exclude(module = "commons-logging")
    }
}

val querydslVersion = dependencyManagement.importedProperties["querydsl.version"]

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.cloud:spring-cloud-commons:4.1.3")
    implementation("org.redisson:redisson-spring-boot-starter:3.45.1")
    implementation("com.hazelcast:hazelcast-spring")
    implementation("com.hazelcast:hazelcast")
    implementation("io.minio:minio:8.5.17")

    implementation("org.hibernate.validator:hibernate-validator")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
    implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.6")
//    implementation("org.slf4j:slf4j-api:2.0.17")
//    implementation("ch.qos.logback:logback-classic:1.5.18")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-framework-datatest:5.9.1")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testImplementation("org.springframework.security:spring-security-test")

    annotationProcessor("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    kapt("org.mapstruct:mapstruct-processor:1.6.3")
    kaptTest("org.mapstruct:mapstruct-processor:1.6.3")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks {
    test {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        dependsOn(test)
        inputs.dir(snippetsDir)
        configurations(asciidoctorExt.name)
        baseDirFollowsSourceFile()
    }

    build {
        dependsOn(asciidoctor)
        doFirst {
            delete("src/main/resources/static/docs")
        }
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }
}
