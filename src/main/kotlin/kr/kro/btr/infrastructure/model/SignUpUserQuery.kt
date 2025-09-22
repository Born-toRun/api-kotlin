package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.RoleType

data class SignUpUserQuery(
    val userId: Long,
    val userName: String,
    val crewId: Long,
    val instagramId: String?,
    val roleType: RoleType
)
