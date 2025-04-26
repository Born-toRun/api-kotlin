package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.entity.UserEntity


data class CreateRefreshTokenQuery(
    val userId: Long,
    val refreshToken: String,
    val userEntity: UserEntity
)
