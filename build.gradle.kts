
import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("org.springframework.boot")
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.epages.restdocs-api-spec") version "0.19.4"
    id("org.hidetake.swagger.generator") version "2.19.2"

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

swaggerSources {
    create("swaggerSource") {
        setInputFile(file("${openapi3.outputDirectory}/openapi3.yaml")) // 올바른 함수 사용
    }
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.6")
    implementation("club.minnced:discord-webhooks:0.8.4")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-framework-datatest:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.epages:restdocs-api-spec:0.19.4")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.4")

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
    swaggerUI("org.webjars:swagger-ui:5.22.0")
}

openapi3 {
    val local = closureOf<Server> {
        url("http://localhost:8080")
        description("Local Server")
    } as Closure<Server>

    val dev = closureOf<Server> {
        url("https://api.born-to-run.kro.kr:8443")
        description("dev Server")
    } as Closure<Server>

    setServers(listOf(local, dev))
    title = "Born-to-run private API"
    description = "본투런 api docs"
    version = "1.0.0"
    format = "yaml"
    outputDirectory = openapi3.outputDirectory
}

tasks.withType<GenerateSwaggerUI> {
    dependsOn("openapi3")
    doFirst {
        val swaggerUIFile = file("${openapi3.outputDirectory}/openapi3.yaml")
        val securitySchemesContent = """
            securitySchemes:
                Authorization:
                  type: apiKey
                  name: Authorization
                  scheme: bearer
                  bearerFormat: JWT
                  in: header
          security:
            - Authorization: []
        """.trimIndent()
        swaggerUIFile.appendText(securitySchemesContent)
    }
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
        dependsOn(generateSwaggerUI)

        doFirst {
            delete("src/main/resources/static")
        }
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
            copy {
                from("build/swagger-ui-swaggerSource")
                into("src/main/resources/static/docs/swagger")
            }
        }
    }
}
