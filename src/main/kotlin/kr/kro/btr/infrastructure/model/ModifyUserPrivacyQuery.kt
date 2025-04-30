package kr.kro.btr.infrastructure.model

data class ModifyUserPrivacyQuery(
    val myUserId: Long,
    val isInstagramIdPublic: Boolean
)
