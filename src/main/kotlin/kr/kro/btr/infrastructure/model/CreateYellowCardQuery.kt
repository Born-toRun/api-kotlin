package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.entity.UserEntity

data class CreateYellowCardQuery(
    val targetUserId: Long,
    val sourceUserId: Long,
    val reason: String?,
    val basis: String,
    val sourceUser: UserEntity?,
    val targetUser: UserEntity?
)