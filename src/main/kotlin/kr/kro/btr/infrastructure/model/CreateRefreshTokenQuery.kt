package kr.kro.btr.infrastructure.model

data class CreateRefreshTokenQuery(
    val userId: Long,
    val refreshToken: String
)
