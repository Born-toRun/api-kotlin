package kr.kro.btr.domain.port.model

data class ModifyUserCommand(
    val userId: Long,
    val profileImageId: Long?,
    val instagramId: String?
)
