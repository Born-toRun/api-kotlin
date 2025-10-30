package kr.kro.btr.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.DisplayName

@DisplayName("레이어 아키텍처 의존성 검증")
@AnalyzeClasses(
    packages = ["kr.kro.btr"],
    importOptions = [
        ImportOption.DoNotIncludeTests::class,
        ExcludeQueryDslClasses::class
    ]
)
class LayeredArchitectureTest {

    @ArchTest
    val layered_architecture_should_be_respected: ArchRule = layeredArchitecture()
        .consideringOnlyDependenciesInLayers()  // 레이어 간 의존성만 검증 (java.lang.Object, kotlin.Metadata 등 제외)
        // 레이어 정의
        .layer("Domain").definedBy("kr.kro.btr.domain..")
        .layer("Core").definedBy("kr.kro.btr.core..")
        .layer("AdapterIn").definedBy("kr.kro.btr.adapter.in..")
        .layer("AdapterOut").definedBy("kr.kro.btr.adapter.out..")
        .layer("Infrastructure").definedBy("kr.kro.btr.infrastructure..")
        .layer("Config").definedBy("kr.kro.btr.config..")
        .layer("Support").definedBy("kr.kro.btr.support..")
        .layer("Base").definedBy("kr.kro.btr.base..")

        // Domain: 아무것도 의존하지 않음
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Core", "AdapterIn", "AdapterOut", "Infrastructure", "Config", "Support", "Base")

        // Core: Domain, Infrastructure, Support, Config를 의존
        .whereLayer("Core").mayOnlyBeAccessedByLayers("AdapterIn", "Config")
        .whereLayer("Core").mayOnlyAccessLayers("Domain", "Infrastructure", "Support", "Base", "Config")

        // AdapterIn: Core와 Domain, Support를 의존
        .whereLayer("AdapterIn").mayOnlyAccessLayers("Core", "Domain", "Support", "Base")

        // AdapterOut: Domain, Infrastructure, Support, Base를 의존
        .whereLayer("AdapterOut").mayOnlyAccessLayers("Domain", "Infrastructure", "Support", "Base")

        // Infrastructure: Domain, AdapterOut, Support, Base, Config를 의존
        .whereLayer("Infrastructure").mayOnlyAccessLayers("Domain", "AdapterOut", "Support", "Base", "Config")

        .because("레이어 간 의존성은 프로젝트의 Gateway 패턴을 준수해야 합니다.")

    @ArchTest
    val domain_layer_should_not_depend_on_any_other_layer: ArchRule = noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "..core..",
            "..adapter..",
            "..infrastructure..",
            "..config.."
        )
        .because("Domain 레이어는 완전히 독립적이어야 합니다 (Support 공통 유틸리티 제외).")

    @ArchTest
    val core_layer_should_not_depend_on_adapter_layer: ArchRule = noClasses()
        .that()
        .resideInAPackage("kr.kro.btr.core..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("kr.kro.btr.adapter..")
        .because("Core는 Port 인터페이스를 통해서만 Adapter와 통신해야 합니다.")

    @ArchTest
    val adapter_in_should_not_depend_on_adapter_out: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.in..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.out..")
        .because("Adapter.In과 Adapter.Out은 서로 격리되어야 하며, Core를 통해서만 통신해야 합니다.")

    @ArchTest
    val adapter_out_should_not_depend_on_adapter_in: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.out..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.in..")
        .because("Adapter.Out은 Adapter.In을 알아서는 안 됩니다.")

    @ArchTest
    val adapter_out_should_not_depend_on_core: ArchRule = noClasses()
        .that()
        .resideInAPackage("kr.kro.btr.adapter.out..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("kr.kro.btr.core..")
        .because("Adapter.Out은 Core를 직접 호출해서는 안 되며, Infrastructure를 통해 접근됩니다.")

    @ArchTest
    val adapter_in_should_not_depend_on_infrastructure: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.in..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..infrastructure..")
        .because("Adapter.In은 Infrastructure 구현 세부사항을 알아서는 안 됩니다.")

    @ArchTest
    val domain_should_not_depend_on_spring_core_framework: ArchRule = noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework.stereotype..",
            "org.springframework.context..",
            "org.springframework.beans..",
            "org.springframework.web..",
            "org.springframework.data.jpa.repository..",
            "org.springframework.transaction.."
        )
        .because("Domain은 Spring Core Framework에 의존하지 않아야 합니다 (JPA, Spring Util은 허용).")

    @ArchTest
    val web_layer_should_not_depend_on_persistence_layer: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.in.web..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.out.persistence..")
        .because("Web 레이어는 Persistence를 직접 호출하지 않고 Core를 통해야 합니다.")

    @ArchTest
    val persistence_layer_should_not_depend_on_web_layer: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.out.persistence..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.in.web..")
        .because("Persistence는 Web을 알아서는 안 됩니다.")

    @ArchTest
    val thirdparty_layer_should_not_depend_on_web_layer: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.out.thirdparty..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.in.web..")
        .because("ThirdParty는 Web을 알아서는 안 됩니다.")

    @ArchTest
    val persistence_should_not_depend_on_thirdparty: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.out.persistence..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.out.thirdparty..")
        .because("Persistence와 ThirdParty는 서로 독립적이어야 합니다.")

    @ArchTest
    val thirdparty_should_not_depend_on_persistence: ArchRule = noClasses()
        .that()
        .resideInAPackage("..adapter.out.thirdparty..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..adapter.out.persistence..")
        .because("ThirdParty와 Persistence는 서로 독립적이어야 합니다.")

    @ArchTest
    val infrastructure_should_not_depend_on_core: ArchRule = noClasses()
        .that()
        .resideInAPackage("kr.kro.btr.infrastructure..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("kr.kro.btr.core..")
        .because("Infrastructure는 Port를 통해 Core에 의해 호출되며, 직접 Core를 호출하지 않습니다.")

    @ArchTest
    val base_should_not_depend_on_core: ArchRule = noClasses()
        .that()
        .resideInAPackage("kr.kro.btr.base..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("kr.kro.btr.core..")
        .because("Base (확장 함수, 유틸리티)는 Core에 의존하지 않아야 합니다.")
}
