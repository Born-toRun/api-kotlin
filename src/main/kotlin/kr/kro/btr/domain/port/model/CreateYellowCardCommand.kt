package kr.kro.btr.domain.port.model

data class CreateYellowCardCommand(
    val targetUserId: Long,
    val sourceUserId: Long,
    val reason: String?,
    val basis: String
)