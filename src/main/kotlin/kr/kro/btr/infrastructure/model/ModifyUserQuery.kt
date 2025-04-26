package kr.kro.btr.infrastructure.model

data class ModifyUserQuery(
    val userId: Long,
    val profileImageId: Long? = null,
    val instagramId: String? = null
)