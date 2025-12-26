package kr.kro.btr.domain.port.model.result

data class RefreshTokenResult(
    val accessToken: String,
    val refreshToken: String,
    val cookieMaxAge: Int
)
