package kr.kro.btr.domain.port.model

data class UserPrivacy(
    val id: Long,
    val userId: Long,
    val isInstagramIdPublic: Boolean
)