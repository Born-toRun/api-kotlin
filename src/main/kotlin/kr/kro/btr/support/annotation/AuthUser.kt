package kr.kro.btr.support.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AuthUser(
    val errorOnInvalidType: Boolean = true
)
