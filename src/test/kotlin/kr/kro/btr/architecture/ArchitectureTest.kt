package kr.kro.btr.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.DisplayName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

@DisplayName("헥사고날 아키텍처 검증")
@AnalyzeClasses(
    packages = ["kr.kro.btr"],
    importOptions = [
        ImportOption.DoNotIncludeTests::class,
        ExcludeQueryDslClasses::class
    ]
)
class ArchitectureTest {

    @ArchTest
    val domain_should_not_depend_on_other_layers: ArchRule = noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "..adapter..",
            "..core..",
            "..infrastructure..",
            "..config.."
        )
        .because("Domain 레이어는 순수한 비즈니스 로직만 포함해야 하며, 외부 레이어에 의존하지 않아야 합니다.")

    @ArchTest
    val domain_should_not_use_spring_annotations: ArchRule = noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework.stereotype..",
            "org.springframework.context..",
            "org.springframework.beans..",
            "org.springframework.web..",
            "org.springframework.transaction.."
        )
        .because("Domain 레이어는 Spring Framework에 의존하지 않아야 합니다.")

    @ArchTest
    val core_should_only_depend_on_domain_infrastructure_and_support: ArchRule = classes()
        .that()
        .resideInAPackage("..core..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(
            "..core..",
            "..domain..",
            "..infrastructure..",
            "..base..",
            "..support..",
            "..config.properties..",
            "java..",
            "kotlin..",
            "org.springframework.stereotype..",
            "org.springframework.transaction..",
            "org.springframework.context..",
            "org.springframework.scheduling..",
            "org.slf4j..",
            "io.github.oshai..",
            "io.jsonwebtoken..",
            "org.springframework.security..",
            "org.jetbrains.annotations.."
        )
        .because("Core 레이어는 Domain, Infrastructure, Support를 의존하며 Adapter는 직접 의존하지 않습니다.")

    @ArchTest
    val adapter_in_should_depend_on_domain_and_core: ArchRule = classes()
        .that()
        .resideInAPackage("..adapter.in..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(
            "..adapter.in..",
            "..domain..",
            "..core..",
            "..base..",
            "..support..",
            "..proxy..",
            "..payload..",
            "java..",
            "kotlin..",
            "org.springframework..",
            "jakarta..",
            "org.slf4j..",
            "io.github.oshai..",
            "com.fasterxml.jackson..",
            "org.jetbrains.annotations.."
        )
        .because("Adapter.in은 Domain과 Core만 호출해야 합니다.")

    @ArchTest
    val adapter_out_should_depend_on_domain: ArchRule = classes()
        .that()
        .resideInAPackage("..adapter.out..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(
            "..adapter.out..",
            "..domain..",
            "..infrastructure..",
            "..base..",
            "..support..",
            "java..",
            "kotlin..",
            "org.springframework..",
            "jakarta..",
            "com.querydsl..",
            "org.slf4j..",
            "io.github.oshai..",
            "com.fasterxml.jackson..",
            "io.minio..",
            "org.redisson..",
            "club.minnced.discord.webhook..",
            "org.jetbrains.annotations.."
        )
        .because("Adapter.out (Persistence, ThirdParty)는 Domain과 Infrastructure만 의존해야 합니다.")

    @ArchTest
    val adapter_in_should_not_depend_on_adapter_out: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.in..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.out..")
        .because("Adapter.in과 Adapter.out은 서로 의존하지 않아야 합니다.")

    @ArchTest
    val adapter_out_should_not_depend_on_adapter_in: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.out..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.in..")
        .because("Adapter.out과 Adapter.in은 서로 의존하지 않아야 합니다.")

    @ArchTest
    val port_interfaces_should_reside_in_domain_port_package: ArchRule = classes()
        .that()
        .haveSimpleNameEndingWith("Port")
        .and()
        .areInterfaces()
        .should()
        .resideInAPackage("..domain.port..")
        .because("Port 인터페이스는 domain.port 패키지에 위치해야 합니다.")

    @ArchTest
    val controllers_should_reside_in_adapter_in_web_and_be_annotated: ArchRule = classes()
        .that()
        .haveSimpleNameEndingWith("Controller")
        .should()
        .resideInAPackage("..adapter.in.web..")
        .andShould()
        .beAnnotatedWith(RestController::class.java)
        .orShould()
        .beAnnotatedWith(Controller::class.java)
        .because("Controller는 adapter.in.web에 위치하고 @RestController 또는 @Controller로 표시되어야 합니다.")

    @ArchTest
    val repositories_should_reside_in_adapter_out_persistence: ArchRule = classes()
        .that()
        .haveSimpleNameEndingWith("Repository")
        .and()
        .areAssignableTo(JpaRepository::class.java)
        .should()
        .resideInAPackage("..adapter.out.persistence..")
        .because("JPA Repository는 adapter.out.persistence 패키지에 위치해야 합니다.")

    @ArchTest
    val services_should_reside_in_core_service_and_be_annotated: ArchRule = classes()
        .that()
        .haveSimpleNameEndingWith("Service")
        .and()
        .resideInAPackage("..core..")
        .and()
        .areNotInterfaces()
        .should()
        .resideInAPackage("..core.service..")
        .andShould()
        .beAnnotatedWith(Service::class.java)
        .because("Service 클래스는 core.service에 위치하고 @Service로 표시되어야 합니다.")

    @ArchTest
    val entities_should_reside_in_domain_entity_package: ArchRule = classes()
        .that()
        .haveSimpleNameEndingWith("Entity")
        .should()
        .resideInAPackage("..domain.entity..")
        .because("Entity 클래스는 domain.entity 패키지에 위치해야 합니다.")

    @ArchTest
    val entities_should_be_annotated_with_entity: ArchRule = classes()
        .that()
        .resideInAPackage("..domain.entity..")
        .and()
        .haveSimpleNameEndingWith("Entity")
        .should()
        .beAnnotatedWith(jakarta.persistence.Entity::class.java)
        .orShould()
        .beAnnotatedWith(jakarta.persistence.Embeddable::class.java)
        .because("Entity 클래스는 @Entity 또는 @Embeddable로 표시되어야 합니다.")

    @ArchTest
    val controller_classes_should_end_with_controller: ArchRule = classes()
        .that()
        .resideInAPackage("..adapter.in.web..")
        .and()
        .areAnnotatedWith(RestController::class.java)
        .or()
        .areAnnotatedWith(Controller::class.java)
        .should()
        .haveSimpleNameEndingWith("Controller")
        .because("Controller 클래스는 'Controller'로 끝나야 합니다.")

    @ArchTest
    val service_classes_should_end_with_service: ArchRule = classes()
        .that()
        .resideInAPackage("..core.service..")
        .and()
        .areAnnotatedWith(Service::class.java)
        .should()
        .haveSimpleNameEndingWith("Service")
        .because("Service 클래스는 'Service'로 끝나야 합니다.")

    @ArchTest
    val repository_interfaces_should_end_with_repository: ArchRule = classes()
        .that()
        .resideInAPackage("..adapter.out.persistence..")
        .and()
        .areAssignableTo(JpaRepository::class.java)
        .should()
        .haveSimpleNameEndingWith("Repository")
        .because("Repository 인터페이스는 'Repository'로 끝나야 합니다.")

    @ArchTest
    val port_interfaces_should_end_with_port: ArchRule = classes()
        .that()
        .resideInAPackage("..domain.port..")
        .and()
        .areInterfaces()
        .and()
        .areNotNestedClasses()
        .should()
        .haveSimpleNameEndingWith("Port")
        .because("Port 인터페이스는 'Port'로 끝나야 합니다.")

    @ArchTest
    val major_layers_should_be_free_of_cycles: ArchRule = com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices()
        .matching("kr.kro.btr.(domain|core|adapter.in)..")
        .should()
        .beFreeOfCycles()
        .because("Domain, Core, AdapterIn 레이어 간 순환 의존성이 없어야 합니다. (Infrastructure <-> AdapterOut의 Gateway 패턴 순환은 허용)")

}
