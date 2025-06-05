package kr.kro.btr.domain.port.model.result

data class UserPrivacyResult(
    val id: Long,
    val userId: Long,
    val isInstagramIdPublic: Boolean
)
