package kr.kro.btr.infrastructure.model

data class SignUpUserQuery(
    val userId: Long,
    val userName: String,
    val crewId: Long,
    val instagramId: String?
)