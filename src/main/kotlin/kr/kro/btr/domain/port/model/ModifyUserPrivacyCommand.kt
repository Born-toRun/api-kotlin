package kr.kro.btr.domain.port.model

data class ModifyUserPrivacyCommand(
    val myUserId: Long,
    val isInstagramIdPublic: Boolean
)