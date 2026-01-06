package kr.kro.btr.domain.port.model

data class KickCrewMemberCommand(
    val crewId: Long,
    val targetUserId: Long,
    val requesterId: Long,
    val isRequesterAdmin: Boolean
)
