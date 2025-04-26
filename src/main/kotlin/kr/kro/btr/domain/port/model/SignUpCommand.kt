package kr.kro.btr.domain.port.model

data class SignUpCommand(
    val userId: Long,
    val userName: String,
    val crewId: Long,
    val instagramId: String?
)