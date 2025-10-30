package kr.kro.btr.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location

class ExcludeQueryDslClasses : ImportOption {
    override fun includes(location: Location): Boolean {
        val locationStr = location.toString()

        // QueryDSL Q클래스 제외
        if (locationStr.contains("/Q") || locationStr.contains("\\Q")) {
            return false
        }

        // Generated 디렉토리 제외
        if (locationStr.contains("/generated/") || locationStr.contains("\\generated\\")) {
            return false
        }

        // QueryDSL 라이브러리 제외
        if (locationStr.contains("com/querydsl/") || locationStr.contains("com\\querydsl\\")) {
            return false
        }

        // JetBrains 애노테이션 제외
        if (locationStr.contains("org/jetbrains/annotations/") || locationStr.contains("org\\jetbrains\\annotations\\")) {
            return false
        }

        return true
    }
}
