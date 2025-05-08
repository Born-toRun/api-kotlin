package kr.kro.btr.infrastructure.model

data class CreateYellowCardQuery(
    val targetUserId: Long,
    val sourceUserId: Long,
    val reason: String?,
    val basis: String
)
