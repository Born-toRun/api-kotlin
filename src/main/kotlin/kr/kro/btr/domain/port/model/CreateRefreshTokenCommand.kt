package kr.kro.btr.domain.port.model

data class CreateRefreshTokenCommand(
    val userId: Long,
    val refreshToken: String
)